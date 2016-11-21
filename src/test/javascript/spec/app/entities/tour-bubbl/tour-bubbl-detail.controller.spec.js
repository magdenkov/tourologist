'use strict';

describe('Controller Tests', function() {

    describe('TourBubbl Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTourBubbl, MockBubbl, MockTour, MockTourBubblRoutePoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTourBubbl = jasmine.createSpy('MockTourBubbl');
            MockBubbl = jasmine.createSpy('MockBubbl');
            MockTour = jasmine.createSpy('MockTour');
            MockTourBubblRoutePoint = jasmine.createSpy('MockTourBubblRoutePoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TourBubbl': MockTourBubbl,
                'Bubbl': MockBubbl,
                'Tour': MockTour,
                'TourBubblRoutePoint': MockTourBubblRoutePoint
            };
            createController = function() {
                $injector.get('$controller')("TourBubblDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:tourBubblUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
