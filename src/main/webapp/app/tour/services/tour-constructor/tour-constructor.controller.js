(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourConstructorController', TourConstructorController);

    TourConstructorController.$inject = ['$timeout', '$scope', 'Bubbl', '$uibModalInstance', 'entity', 'Tour', 'User', 'Interest', 'ParseLinks',
        'Principal', 'uiGmapIsReady', 'TourMapContextMenuService', 'ConfirmDeleteBubblTourConstructorService'];

    function TourConstructorController($timeout, $scope, Bubbl, $uibModalInstance, entity, Tour, User, Interest, ParseLinks,
                                       Principal, uiGmapIsReady, mapContextMenu, confirmDeleteBubbl) {
        var vm = this;

        vm.mapControl = null;
        vm.showBubblesInRadius = {
            show: true,
            value: 10000
        };
        vm.tour = entity;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
            mapContextMenu.init(vm.mapControl);
            drawTourOnMap();
        })

        vm.sortableOptions = {
            update: function (e, ui) {
                // alert(e);
            },
            stop: function (e, ui) {
                // alert(e);
            }
        };

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        vm.mapContextMenuUrl = 'app/tour/services/tour-constructor/map-context-menu.html';

        // tabs

        vm.tabs = [
            {
                title: 'Main Info',
                url: 'app/tour/services/tour-constructor/tabs/tour-main.tab.html'
            }, {
                title: 'Tour Bubbls',
                url: 'app/tour/services/tour-constructor/tabs/tour-bubbls.tab.html'
            }, {
                title: 'Tour Details',
                url: 'app/tour/services/tour-constructor/tabs/tour-details.tab.html'
            }];

        vm.currentTab = vm.tabs[0];

        vm.onClickTab = function (tab) {
            vm.currentTab = tab;
        }

        vm.isActiveTab = function (tab) {
            return tab == vm.currentTab;
        }

        // tabs end

        vm.save = function () {
            vm.isSaving = true;

            var i = 0;

            var _bubbls = _.map(vm.tour.bubbls, function (bubbl) {
                return {
                    orderNumber: i++,
                    bubblId: +bubbl.id
                }
            });

            var params = {
                bubbls: _bubbls,
                description: vm.tour.description,
                id: vm.tour.id,
                interests: vm.tour.interests,
                name: vm.tour.name
            }

            if (vm.tour.id !== null) {
                Tour.updateFixedTour(params, onSaveSuccess, onSaveError);
            } else {
                Tour.createFixedTour(params, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:tourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.users = User.query();
        vm.interests = Interest.query();

        vm.removeBubbl = function (bubbl) {
            confirmDeleteBubbl.call(bubbl).then(function (result) {
                if (result === true) {
                    vm.tour.bubbls.splice(_.findIndex(vm.tour.bubbls, bubbl), 1);
                }
            })

        }

        vm.bubbls = [];

        vm.loadPage = function (page) {
            vm.page = page;
        }

        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.size = 250;
        vm.reset = reset;
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
                page: vm.page,
                size: vm.size,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.bubbls.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            Bubbl.query({
                page: vm.page,
                size: vm.size,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.bubbls.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.recipes = [];
            changeUrl();
        }

        $scope.user = {
            interest: [vm.interests[0]]
        };

        if (vm.tour.bubbls == null) {
            vm.tour.bubbls = [];
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.onBubblMarkerRightClick = function (bubbl, event) {
            event.bubbl = bubbl;
            mapContextMenu.show(event);
        }

        vm.onAddBubblToTourClick = function () {
            mapContextMenu.close();
            var bubbl = mapContextMenu.currentEvent().bubbl;
            if (_.find(vm.tour.bubbls, {id: bubbl.id}) == null) {
                $timeout(function () {
                    vm.tour.bubbls.push(bubbl);
                })
            } else {
                alert("This Bubble is already added")
            }

        }

        vm.onRemoveBubblFromTourClick = function () {
            mapContextMenu.close();
            var bubbl = mapContextMenu.currentEvent().bubbl;
            if (_.find(vm.tour.bubbls, {id: bubbl.id}) != null) {
                $timeout(function () {
                    vm.tour.bubbls.splice(_.findIndex(vm.tour.bubbls, bubbl), 1);
                })
            } else {
                alert("This Bubble is not in Tour");
            }
        }

        $scope.$watch('vm.tour.bubbls', function () {
            console.log("!DD");
            drawTourOnMap();
        }, true);

        vm.route = null;

        var drawTourOnMap = function () {
            if (vm.mapControl != null) {
                var clear = function () {
                    if (vm.route && vm.route.way) {
                        vm.route.way.setMap(null);
                        vm.route.way = null;
                    }
                    if (vm.route && vm.route.bubbls) {
                        vm.route.bubbls.forEach(function (bubbl) {
                            bubbl.setMap(null);
                        })
                        vm.route.bubbls = [];
                    }
                }

                clear();

                var coordinates = [];
                var bubbls = [];

                vm.tour.bubbls.forEach(function (bubble) {
                    var coord = new google.maps.LatLng(bubble.lat, bubble.lng);
                    coordinates.push(coord);

                    var bubbleMarker = new MarkerWithLabel({
                        position: coord,
                        title: bubble.name,
                        labelContent: bubble.name,
                        labelClass: "labels",
                        labelStyle: {
                            opacity: 0.75,
                            color: "yellow",
                            background: 'black',
                            padding: '2px',
                            margin: '2px'
                        },
                        zIndex: 999999,
                        icon: {
                            path: google.maps.SymbolPath.CIRCLE,
                            scale: 3,
                            strokeWeight: 3,
                            strokeColor: "blue"
                        },
                        map: vm.mapControl,
                        strokeColor: "#1637F5"
                    });
                    bubbls.push(bubbleMarker);
                })

                var way = new google.maps.Polyline({
                    path: coordinates,
                    geodesic: true,
                    strokeColor: 'red',
                    strokeOpacity: 0.5,
                    strokeWeight: 10,
                    map: vm.mapControl,
                    title: 'way'
                });

                vm.route = {
                    way: way,
                    bubbls: bubbls
                }
            }
        }


    }
})();
