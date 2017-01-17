(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourScrollTo', ['$location', '$anchorScroll', tourScrollTo]);

    function tourScrollTo($location, $anchorScroll) {
        return {
            restrict: 'AC',
            link: function (scope, el, attr) {
                el.on('click', function (e) {
                    $location.hash(attr.tourScrollTo);
                    $anchorScroll();
                });
            }
        };
    };

})();

