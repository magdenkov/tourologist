(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourGoogleMap', tourGoogleMap);

    tourGoogleMap.$inject = [];

    function tourGoogleMap() {
        return {
            restrict: 'AE',
            templateUrl: 'app/directives/tour-google-map/tour-google-map.html',
            scope: {},
            link: function (scope, element, attrs) {},
            controller: ['$scope', 'InitialMapConfig', controller],
            controllerAs: 'vm'
        }
    }

    function controller(scope, initialMapConfig) {
        scope.mapConfig = scope.mapConfig || initialMapConfig.call();

        var events = {
            places_changed: function (searchBox) {
                var place = searchBox.getPlaces();
                if (!place || place == 'undefined' || place.length == 0) {
                    console.log('no place data :(');
                    return;
                }

                scope.mapConfig = initialMapConfig.call(place[0].geometry.location.lat(), place[0].geometry.location.lng(), 18);
            }
        };

        scope.searchbox = {template: 'app/directives/tour-google-map/searchbox.tpl.html', events: events};
    }

})();

