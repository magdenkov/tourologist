'use strict';

describe('Controller Tests', function() {

    describe('TourRoutePoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTourRoutePoint, MockTour;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTourRoutePoint = jasmine.createSpy('MockTourRoutePoint');
            MockTour = jasmine.createSpy('MockTour');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TourRoutePoint': MockTourRoutePoint,
                'Tour': MockTour
            };
            createController = function() {
                $injector.get('$controller')("TourRoutePointDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:tourRoutePointUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
