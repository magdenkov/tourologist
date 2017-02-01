(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('AddBubblManuallyService', AddBubblManuallyService);

    AddBubblManuallyService.$inject = ['$q', 'ModalService', 'ValidationService', 'Bubbl'];

    function AddBubblManuallyService($q, modal, validate, Bubbl) {
        var service = this;
        service.items = [];

        service.call = function () {
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
        }

        service.validate = function (subject) {
            subject.errors = {};
            validate.presence(subject, 'subject.bubbl', 'Select Bubbl to add');
            return _.isEmpty(subject.errors);
        };
    }


})();
