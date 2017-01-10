(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .controller('ModalController', ['$uibModalInstance', '$q', 'subject', 'modalParams', 'BackendErrorsResolver', ModalController]);

    function ModalController($modalInstance, $q, subject, modalParams, errorsResolver) {
        var vm = this;

        vm.subject = subject || {};
        vm.modalErrors = [];

        /////////////////////////////////////////////////

        vm.ok = function () {
            if (vm.validateSubject(vm.subject) === true) {
                vm.processing = true;
                vm.process(vm.subject).then(function (response) {
                    if (!vm.notCloseDialogAfterOk) {
                        $modalInstance.close(_.merge(vm.subject, response));
                    }
                }, function (response) {
                    vm.processing = false;
                    if (response && !response.config.customErrors) {
                        vm.modalErrors = vm.resolveErrors(response, vm.subject);
                    }
                });
            }
        };

        vm.validateSubject = function () {
            subject.errors = {};
            vm.validate(subject);
            return _.isEmpty(vm.subject.errors);
        };

        vm.validate = function (subject) {
        };

        vm.process = function (subject) {
            var deferred = $q.defer();
            deferred.resolve(subject);
            return deferred.promise;
        };

        vm.resolveErrors = function (response, subject) {
            return errorsResolver.resolve(response, subject);
        };

        vm.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        vm.closeAfterSomeOperation = function (operationPromise, closeAfterOperationComplete, shouldValidateBeforeOperation) {
            var closeAfterOperationComplete = closeAfterOperationComplete || true;
            var shouldValidateBeforeOperation = shouldValidateBeforeOperation || false;

            if (!shouldValidateBeforeOperation || vm.validateSubject(vm.subject) === true) {
                vm.processing = true;
                operationPromise(vm.subject).then(function (response) {
                    if (closeAfterOperationComplete) {
                        $modalInstance.close(_.merge(vm.subject, response));
                    }
                }, function (response) {
                    vm.modalErrors = errorsResolver.resolve(response, vm.subject);
                }).finally(function () {
                    vm.processing = false;
                });
            }
        }


        /////////////////////////////////////////////////

        angular.extend(vm, modalParams);
    }

})();
