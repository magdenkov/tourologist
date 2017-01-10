(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourModal', tourModal);

    function tourModal() {
        return {
            restrict: 'E',
            transclude: true,
            templateUrl: 'app/directives/tour-modal/tour-modal.html',
            link: function (scope, element, attrs) {
                scope.title = attrs.title;
                scope.okText = attrs.okText;
                scope.processingText = attrs.processingText;
                scope.cancelText = attrs.cancelText;
                scope.btnOkType = attrs.btnOkType || 'btn-primary';
            }
        }
    }

})();
