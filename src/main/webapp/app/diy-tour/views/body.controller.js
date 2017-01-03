(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['$scope', '$state', 'uiGmapIsReady', 'InitialMapConfigForDiyTour'];

    function DiyTourController($scope, $state, uiGmapIsReady, initialMapConfig) {
        var vm = this;

        vm.mapControl = null;
        vm.mapConfig = initialMapConfig.call();

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
        })

        var events = {
            places_changed: function (searchBox) {
                var place = searchBox.getPlaces();
                if (!place || place == 'undefined' || place.length == 0) {
                    console.log('no place data :(');
                    return;
                }

                vm.mapConfig = initialMapConfig.call(place[0].geometry.location.lat(), place[0].geometry.location.lng(), 18);

            }
        };

        vm.searchbox = {template: 'app/diy-tour/views/searchbox.tpl.html', events: events};
    }
})();
