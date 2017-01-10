(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('BackendErrorsResolver', ['$log', 'BackendUrlResolver', BackendErrorsResolver]);

    function BackendErrorsResolver(logger, urlResolver) {
        var service = this;

        var ALLOWED_ERROR_STATUSES = [406, 422, 400];

        //////////////////////////////////////////////////

        service.resolve = function (response, templateData, translations) {
            return _.map(service.resolveWithTypes(response, templateData, translations), 'text');
        };

        service.resolveWithTypes = function (response, templateData, translations) {
            if (!allowedError(response)) return [];

            var requestUrl = urlResolver.unresolve(response.config.url);
            var errors = extractErrors(response);

            return _.map(errors, function (error) {
                return translateError(error, requestUrl, templateData, translations);
            });
        };

        //////////////////////////////////////////////////

        function allowedError(response) {
            return ALLOWED_ERROR_STATUSES.indexOf(response.status) > -1;
        };

        function extractErrors(response) {
            var errors = [];
            if (_.isObject(response.data)) {
                _.each(_.keys(response.data), function (field) {
                    _.each(response.data[field], function (error) {
                        errors.push([field + ':' + error.type, error.message]);
                    });
                });
            } else if (_.isArray(response.data)) {
                errors = _.map(response.data, function (error) {
                    return [error, error]
                });
            } else {
                errors = [[response.data.trim(), response.data.trim()]];
            }

            return errors;
        };

        function translateError(error, url, templateData, translations) {
            var errorCode = error[0];
            var errorMessage = error[1];

            // if errorMessage is not message but json string which contains message propery
            try {
                var errorObject = angular.fromJson(errorMessage);
                if (errorObject && errorObject.message) {
                    errorMessage = errorObject.message;
                }
            } catch (e) {
            }
            //

            var templateData = templateData || {};

            var translatedMessage = translate(url.replace(/[0-9]/g, '').replace(/\/$/, '') + ':' + errorCode, translations);
            if (!translatedMessage) {
                logger.warn('Untranslated error with code: ' + url + ':' + errorCode);
                return {
                    type: "raw",
                    text: errorMessage
                };
            } else {
                return {
                    type: "translated",
                    text: _.template(translatedMessage)(_.merge({}, templateData, {message: errorMessage}))
                };
            }
        };

        function translate(code, translations) {
            return {
                    // DEPRECATED. DON'T APPEND ERRORS TO THIS HASH. USE translations PARAMETER
                }[code] || (translations || {})[code];
        };
    }

})();
