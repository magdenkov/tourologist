(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('CreateTourController', TourDialogController);

    TourDialogController.$inject = ['$timeout', '$scope', 'Bubbl', '$uibModalInstance', 'entity', 'Tour', 'User', 'Interest'];

    function TourDialogController($timeout, $scope, Bubbl, $uibModalInstance, entity, Tour, User, Interest) {
        var vm = this;

        vm.tour = entity;
        vm.clear = clear;

        vm.save = save;
        vm.users = User.query();
        vm.interests = Interest.query();

        vm.bubbls = Bubbl.query();
        vm.add = add;
        vm.removebubbl = removebubbl;

        $scope.user = {
            interest: [vm.interests[0]]
        };
        if (vm.tour.bubbls === undefined) {
            vm.tour.bubbls = [];
        }

        if (vm.tour.bubbls.length === 0) {
            vm.tour.bubbls.push({
                bubblId: '',
                orderNumber: ''
            });
        }


        function removebubbl(bubblIndex) {
            console.log('removing ingredient with id ' + bubblIndex);
            vm.tour.bubbls.splice(bubblIndex, 1);
        }

        function add() {
            vm.tour.bubbls.push({
                bubblId: '',
                orderNumber: ''
            });
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            Tour.createFixedTour(vm.tour, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:tourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
