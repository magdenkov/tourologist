(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('BackendUrlResolver', ['$location', BackendUrlResolver]);

    function BackendUrlResolver($location) {
        var service = this;

        var subdomain = $location.host().split('.')[0];
        var baseUrl = {
            'mock': '//mocks/',
            'test': '//mocks/',
            'development': 'http://<subdomain>.someurl.me:8888/',
            'docker': 'http://<subdomain>.someurl.me/v1/',
            'testing': 'https://<subdomain>.someurl.co/v1/',
            'staging': 'https://<subdomain>.someurl.co/v1/',
            'production': 'https://<subdomain>.someurl.com/v1/'
        }['testing']

        service.resolve = function (path) {
            return path.match(/html$/) ? path :
                path.match(/^\/$/) ? baseUrl.replace('<subdomain>', subdomain) :
                    path.match(/login$/) ? baseUrl.replace('<subdomain>', subdomain) + path :
                        path.match(/logout$/) ? baseUrl.replace('<subdomain>', subdomain) + path :
                            baseUrl.match(/mocks\/$/) ? baseUrl + path :
                            baseUrl.replace('<subdomain>', subdomain) + path;
        };

        service.unresolve = function (path) {
            return path.match(/html$/) ? path :
                path.match(/^\/$/) ? path.replace(baseUrl.replace('<subdomain>', subdomain), '') :
                    baseUrl.match(/mocks\/$/) ? path.replace(baseUrl, '') :
                        path.replace(baseUrl.replace('<subdomain>', subdomain), '');
        };
    }

})();

