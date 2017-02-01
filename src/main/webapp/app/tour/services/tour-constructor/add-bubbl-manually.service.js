(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('AddBubblManuallyService', AddBubblManuallyService);

    AddBubblManuallyService.$inject = ['$rootScope', '$q', 'ModalService', 'ValidationService', 'Bubbl', 'Principal'];

    function AddBubblManuallyService($rootScope, $q, modal, validate, Bubbl, Principal) {
        var service = this;
        service.items = [];
        service.account = null;
        service.isAuthenticated = null;
        service.call = call;
        service.callAdmin = callAdmin;
        $rootScope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                service.account = account;
                service.isAuthenticated = Principal.isAuthenticated;
                changeUrl()
            });
        }

        function changeUrl() {
            if (service.account.authorities.includes('ROLE_ADMIN')) {
                callAdmin();
            } else {
                call();
            }
        }

        function callAdmin() {
            var deferred = $q.defer();

            Bubbl.queryAdmin({page: 0, size: 500}).$promise.then(function (bubbls) {
                service.items = bubbls;

                modal.open('app/tour/services/tour-constructor/add-bubbl-manually.modal.html',
                    {
                        bubbl: null
                    }, {
                        items: service.items,
                        validate: service.validate
                    }
                ).then(
                    function (result) {
                        deferred.resolve(result.bubbl);
                    }, function () {
                        deferred.resolve(null);
                    });
            }, function (error) {
                deferred.reject(error);
            });

            return deferred.promise;
        };

        function call() {
            var deferred = $q.defer();

            Bubbl.query({page: 0, size: 500}).$promise.then(function (bubbls) {
                service.items = bubbls;

                modal.open('app/tour/services/tour-constructor/add-bubbl-manually.modal.html',
                    {
                        bubbl: null
                    }, {
                        items: service.items,
                        validate: service.validate
                    }
                ).then(
                    function (result) {
                        deferred.resolve(result.bubbl);
                    }, function () {
                        deferred.resolve(null);
                    });
            }, function (error) {
                deferred.reject(error);
            });

            return deferred.promise;
        };

        service.validate = function (subject) {
            subject.errors = {};
            validate.presence(subject, 'subject.bubbl', 'Select Bubbl to add');
            return _.isEmpty(subject.errors);
        };
    }


})();
