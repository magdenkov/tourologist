(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourErrors', ['$compile', '$interpolate', tourErrors]);

    function tourErrors($compile, $interpolate) {
        return {
            restrict: 'A',
            terminal: true,
            priority: 1000,
            link: function (scope, element, attrs) {
                var tourErrors = $interpolate(attrs.tourErrors)(scope);
                var errorsEl = angular.element('<div class="error-message" ng-show="' + tourErrors + '">{{ ' + tourErrors + ' }}</div>');
                if (attrs['tourPlace'] == 'parent') {
                    angular.element(errorsEl).insertAfter(angular.element(element).parent());
                } else {
                    angular.element(errorsEl).insertAfter(element);
                }
                $compile(errorsEl)(scope);

                element.attr("ng-class", '{error: ' + tourErrors + '}');
                element.removeAttr('tour-errors');
                $compile(element)(scope);
            }
        }
    }

})();
