(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblController', BubblController);

    BubblController.$inject = ['$scope', '$state', 'Bubbl', 'ParseLinks', 'AlertService', 'pagingParams',
        'paginationConstants', '$rootScope', 'angularGridInstance', '$timeout', '$q', 'uiGmapIsReady', 'Principal', '$filter'];

    function BubblController($scope, $state, Bubbl, ParseLinks, AlertService, pagingParams, paginationConstants,
                             $rootScope, angularGridInstance, $timeout, $q, uiGmapIsReady, Principal, $filter) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $scope.mapControl = {};


        vm.bubbls = [];


        var circles = [];
        var markers = [];
        $timeout(function () {

            $scope.map = {
                center: {latitude: 54.00366, longitude: -2.547855},
                zoom: 10,
                events: {
                    tilesloaded: function (map, eventname, args) {
                        $scope.mapInstance = map;
                    }
                },
                options: {
                    disableDefaultUI: true,
                    scrollwheel: true,
                }
            };
        }, 1000);


        $scope.mapHeight = function () {
            return $(window).height() - 210;
        };
        $scope.swapbubbls = function () {
            $rootScope.multiple = !$rootScope.multiple;
            loadAll();
            angularGridInstance.bubbls.refresh();
            $scope.mapControl.refresh();
        };

        vm.account = null;
        vm.isAuthenticated = null;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                changeUrl()
            });


        }

        function changeUrl() {

            if (vm.account.authorities.includes('ROLE_ADMIN')) {
                loadAdmin();
            } else {
                loadAll();
            }


        }


        function loadAdmin() {

            Bubbl.queryAdmin({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.bubbls = data;
                vm.page = pagingParams.page;

                $scope.searchBubbls = '';
                $scope.queryBy = 'name';

                getBubblMap();
            }


            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {

            Bubbl.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.bubbls = data;
                vm.page = pagingParams.page;


                getBubblMap();
            }


            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        $scope.showdeletebutton = false;

        $scope.delete = function () {
            $scope.selectedBubbls = [];
            angular.forEach(vm.bubbls, function (bubbl) {
                if (bubbl.selected) $scope.selectedBubbls.push(bubbl.id);

            });
            Bubbl.delete({id: $scope.selectedBubbls});
            console.log($scope.selectedBubbls)
        };


        function getBubblMap() {

            $scope.bubblmap = vm.bubbls;

            function drawMapMarkers() {
                $q.all([uiGmapIsReady.promise(), $scope.bubblmap])
                    .spread(function (maps, data) {
                        var bounds = new google.maps.LatLngBounds();
                        var map = maps[0].map;
                        data.forEach(function (bubbl) {
                            var sbubbl = bubbl.toGooglePolygon({
                                strokeColor: '#3f51b5',
                                fillColor: '#00BFA5',
                                strokeWeight: 1,
                                fillOpacity: .1
                            });
                            sbubbl.setMap(map);
                            circles.push(sbubbl);
                            bubbl.toGoogleLatLngArray().forEach(function (ll) {
                                bounds.extend(ll);
                            });

                            markers.push(new google.maps.Marker({
                                position: bubbl.getCenter(),
                                map: map,
                                title: bubbl.name,

                            }));
                            map.fitBounds(bounds);
                            $scope.map.center = {
                                latitude: bounds.getCenter().lat(),
                                longitude: bounds.getCenter().lng()
                            };
                        });
                        google.maps.event.trigger($scope.map, 'refresh')

                    });
            }

            drawMapMarkers();
            $timeout(drawMapMarkers, 1000);
        }


        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
