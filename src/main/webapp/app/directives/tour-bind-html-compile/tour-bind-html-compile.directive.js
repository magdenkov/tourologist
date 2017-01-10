(function () {
    'use strict';


    angular
        .module('tourologistApp.directives')
        .directive('tourBindHtmlCompile', ['$compile', tourBindHtmlCompile]);

    function tourBindHtmlCompile($compile) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                scope.$watch(function () {
                    return scope.$eval(attrs.tourBindHtmlCompile);
                }, function (value) {
                    element.html(value);
                    $compile(element.contents())(scope);
                });
            }
        };
    }

})();
