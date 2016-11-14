'use strict';

describe('Controller Tests', function() {

    describe('Tour Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTour, MockUser, MockInterest, MockBubbl, MockTourImage, MockTourRoutePoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTour = jasmine.createSpy('MockTour');
            MockUser = jasmine.createSpy('MockUser');
            MockInterest = jasmine.createSpy('MockInterest');
            MockBubbl = jasmine.createSpy('MockBubbl');
            MockTourImage = jasmine.createSpy('MockTourImage');
            MockTourRoutePoint = jasmine.createSpy('MockTourRoutePoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tour': MockTour,
                'User': MockUser,
                'Interest': MockInterest,
                'Bubbl': MockBubbl,
                'TourImage': MockTourImage,
                'TourRoutePoint': MockTourRoutePoint
            };
            createController = function() {
                $injector.get('$controller')("TourDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:tourUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
