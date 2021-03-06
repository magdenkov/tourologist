package tech.bubbl.tourologist.service.impl;

import com.amazonaws.services.kms.model.UnsupportedOperationException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.User;
import tech.bubbl.tourologist.domain.enumeration.PayloadType;
import tech.bubbl.tourologist.repository.BubblRepository;
import tech.bubbl.tourologist.repository.UserRepository;
import tech.bubbl.tourologist.security.AuthoritiesConstants;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.domain.Payload;
import tech.bubbl.tourologist.repository.PayloadRepository;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
import tech.bubbl.tourologist.service.mapper.PayloadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import tech.bubbl.tourologist.web.rest.util.MimeTypesUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Payload.
 */
@Service
public class PayloadServiceImpl implements PayloadService{

    public static final int BYTES_IN_KB = 1024;

    private final Logger log = LoggerFactory.getLogger(PayloadServiceImpl.class);

    public static String AWS_S3_BUCKET_CONST = "AWS_S3_BUCKET";

    @Inject
    private PayloadRepository payloadRepository;

    @Inject
    private PayloadMapper payloadMapper;

    public static String AWS_S3_BUCKET = getProp(AWS_S3_BUCKET_CONST, "tl-audio");

    @Autowired
    private AmazonS3 s3;

    @Inject
    private TransferManager transferManager;

    @Inject
    private UserRepository userRepository;


    @Inject
    private BubblRepository bubblRepository;

    public static String getProp(String property, String defaultP) {
        String env = System.getProperty(property);
        if (env == null)
            return defaultP;
        return env;
    }



    @Transactional
    public PayloadDTO save(PayloadDTO payloadDTO, MultipartFile file) throws UnsupportedOperationException, InterruptedException, IOException {
        Bubbl bubbl = bubblRepository.findOne(payloadDTO.getBubblId());
        if (bubbl == null) {
            throw new EntityNotFoundException("Bubbl with id was not fount " + payloadDTO.getBubblId());
        }

        if (payloadDTO.getPayloadType() == PayloadType.AUDIO && !bubbl.getPayloads().isEmpty()) {
            // remove old audio payloads so each bubbl can have only one AUDIO Payload
            bubbl.getPayloads().stream()
                .filter(payload -> PayloadType.AUDIO.equals(payload.getPayloadType()))
                .forEach(payload -> {
                    deletePayloadFromAWS(payload.getUrl() , payload.getThumbUrl());
                    payloadRepository.delete(payload);
                });
        }

        log.debug("Request to save Payload : {}", payloadDTO.getName());
        log.debug("File size of uploading to S3 file is {}", file.getSize() / BYTES_IN_KB);

        byte[] fileBytes = file.getBytes();
        ObjectMetadata data = getMetadata(payloadDTO.getName(),  fileBytes);
        UploadResult result = uploadFile(payloadDTO, data, fileBytes);

        Payload payload = new Payload();
        payload.setName(payloadDTO.getName());
        payload.setMimeType(payloadDTO.getMimeType());
        payload.setBubbl(bubbl);
        payload.setCreatedDate(ZonedDateTime.now());
        payload.setLastModified(ZonedDateTime.now());
        payload.setPayloadType(payloadDTO.getPayloadType());

        payload.setUrl(amazonUrl(result, false));


//        Payload payload = payloadMapper.payloadDTOToPayload(payloadDTO);
        payload = payloadRepository.save(payload);

        bubbl.getPayloads().clear();
        bubbl.getPayloads().add(payload);
        bubblRepository.save(bubbl);

        PayloadDTO resultDTO = payloadMapper.payloadToPayloadDTO(payload);

        return resultDTO;
    }

    public String amazonUrl(UploadResult result, Boolean isThumb) {
        StringBuilder sb = new StringBuilder().append("https://");
        sb.append("s3-")
            .append(s3.getBucketLocation(result.getBucketName()))
            .append(".amazonaws.com/")
            .append(result.getBucketName())
            .append("/");
//        if (isThumb) {
//            sb.append(image.gets3ThumbKey()).toString();
//        }  else {
            sb.append(result.getKey());
//        }
        // TODO: 29.11.2016 handle thumb uploading
        return sb.toString();
    }


    protected String getUniqueKey(String filename) {
        return getUniqueKey(filename, true);
    }

    protected String getUniqueKey(String filename, boolean keepExt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UUID.randomUUID());
        if (keepExt) {
            stringBuilder.append(".")
                .append(StringUtils.getFilenameExtension(filename));
        }
        return stringBuilder.toString();
    }

    protected String getS3Key(String filename, Long recipeId) {
        StringBuilder builder = new StringBuilder();
        builder.append(recipeId).append("/");
        builder.append(getUniqueKey(filename));
        return builder.toString();
    }


    protected UploadResult uploadFile(PayloadDTO image, ObjectMetadata data, byte[] fileBytes) throws InterruptedException {
        return uploadFile(getS3Key(image.getName(), image.getBubblId()), data, fileBytes);
    }

    protected UploadResult uploadFile(String s3Key, ObjectMetadata data, byte[] fileBytes) throws InterruptedException {

        PutObjectRequest putObjectRequest = new PutObjectRequest(AWS_S3_BUCKET, s3Key, new ByteArrayInputStream(fileBytes), data)
            .withCannedAcl(CannedAccessControlList.PublicRead);
        Upload upload = transferManager.upload(putObjectRequest);
        return upload.waitForUploadResult();
    }

    protected ObjectMetadata getMetadata(String fileName,  byte[] fileBytes) {
        return getMetadata(fileBytes.length,
            MimeTypesUtils.lookupMimeType(StringUtils.getFilenameExtension(fileName)));
    }

    protected ObjectMetadata getMetadata(long length, String contentType) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(length);
        meta.setContentType(contentType);
        return meta;
    }


    /**
     *  Get all the payloads.
     *
     *
     * @param userId
     * @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PayloadDTO> findAll(Long userId, Pageable pageable) {
        log.debug("Request to get all Payloads");

//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        Specification<Payload> specification = getPayloadSpecification(userId);

        Page<Payload> result = payloadRepository.findAll(specification, pageable);
        return result.map(payload -> payloadMapper.payloadToPayloadDTO(payload));
    }

    private Specification<Payload> getPayloadSpecification(final Long userId) {
        return Specifications.where(new Specification<Payload>() {
                @Override
                public Predicate toPredicate(Root<Payload> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();

                    if (userId != null) {
                        predicates.add(cb.equal(root.get("bubbl").get("user").get("id"), userId));
                    }
    //                if (!isAdmin) {   // simple users can see only their own tours
    //                    predicates.add(cb.equal(root.get("bubbl").get("user").get("login"), SecurityUtils.getCurrentUserLogin()));
    //                }

                    return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            });
    }

    /**
     *  Get one payload by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PayloadDTO findOne(Long id) {
        log.debug("Request to get Payload : {}", id);
        Payload payload = payloadRepository.findOne(id);
        PayloadDTO payloadDTO = payloadMapper.payloadToPayloadDTO(payload);
        return payloadDTO;
    }

    /**
     *  Delete the  payload by id.
     *
     *  @param id the id of the entity
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Payload : {}", id);
        Payload payload = payloadRepository.findOne(id);
//        Optional.ofNullable(payload)
//            .orElseThrow(() -> new EntityNotFoundException("Payload w/ id was not found" + id));

        if (payload == null) {
            log.error("Failed to delete payload w/ id : {}  404 NOT FOUND!", id);
            return;
        }

        deletePayloadFromAWS(payload.getUrl() , payload.getThumbUrl());

        payloadRepository.delete(id);
    }


    public void deleteWithoutTransaction(Payload payload) {
        deletePayloadFromAWS(payload.getUrl() , payload.getThumbUrl());
        payloadRepository.delete(payload);
    }



    public Boolean deletePayloadFromAWS(String ... urls) {
        if (urls == null || urls.length == 0) {
            return false;
        }
        String s3Bucket = new AmazonS3URI(urls[0]).getBucket();

        List<String> s3KeysToDelete = Arrays.stream(urls)
            .filter(s -> !Strings.isNullOrEmpty(s))
            .map(s -> new AmazonS3URI(s).getKey())
            .collect(Collectors.toList());


        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(s3Bucket)
            .withKeys(s3KeysToDelete.toArray(new String[s3KeysToDelete.size()]));
        DeleteObjectsResult deleteObjectsResult = s3.deleteObjects(deleteObjectsRequest);

        return true;
    }

    @Override
    @Transactional
    public PayloadDTO save(PayloadDTO payloadDTO) {
        log.debug("Request to save Payload : {}", payloadDTO.getName());


        Payload payload = payloadMapper.payloadDTOToPayload(payloadDTO);
        payload = payloadRepository.save(payload);
        PayloadDTO result = payloadMapper.payloadToPayloadDTO(payload);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayloadDTO> findOnlyMyPayloads(Pageable pageable) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Specification<Payload> specification = getPayloadSpecification(user.getId());

        Page<Payload> result = payloadRepository.findAll(specification, pageable);
        return result.map(payload -> payloadMapper.payloadToPayloadDTO(payload));
    }
}
