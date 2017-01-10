(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('ModalService', ['$uibModal', ModalService]);

    function ModalService($uibModal) {
        var service = this;

        //////////////////////////////////////////////////

        service.openModal = function (templateUrl, subject, modalParams, displayOptions, controllerName) {
            var displayOptions = displayOptions || {};

            return $uibModal.open({
                templateUrl: templateUrl,
                controller: controllerName || 'ModalController',
                controllerAs: 'vm',
                animation: true,
                backdrop: displayOptions.backdrop || 'static',
                windowClass: displayOptions.windowClass || '',
                size: displayOptions.size || 'md',
                resolve: {
                    subject: function () {
                        return subject || {};
                    },
                    modalParams: function () {
                        return modalParams || {};
                    }
                }
            });
        };

        service.open = function (templateUrl, subject, modalParams, displayOptions, controllerName) {
            return this.openModal(templateUrl, subject, modalParams, displayOptions, controllerName).result;
        };
    }

})();
