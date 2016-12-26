package tech.bubbl.tourologist.web.rest.util;

import tech.bubbl.tourologist.service.dto.TransportObject;

/**
 * Created by Denis Magdenkov on 26.12.2016.
 */
public class PageDTO implements TransportObject {

    private  Integer page;

    private Integer size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageDTO)) return false;

        PageDTO pageDTO = (PageDTO) o;

        if (getPage() != null ? !getPage().equals(pageDTO.getPage()) : pageDTO.getPage() != null) return false;
        return getSize() != null ? getSize().equals(pageDTO.getSize()) : pageDTO.getSize() == null;

    }

    @Override
    public int hashCode() {
        int result = getPage() != null ? getPage().hashCode() : 0;
        result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
        return result;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
