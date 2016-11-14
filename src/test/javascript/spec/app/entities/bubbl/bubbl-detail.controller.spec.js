'use strict';

describe('Controller Tests', function() {

    describe('Bubbl Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBubbl, MockUser, MockTour, MockPayload, MockBubblImage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBubbl = jasmine.createSpy('MockBubbl');
            MockUser = jasmine.createSpy('MockUser');
            MockTour = jasmine.createSpy('MockTour');
            MockPayload = jasmine.createSpy('MockPayload');
            MockBubblImage = jasmine.createSpy('MockBubblImage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bubbl': MockBubbl,
                'User': MockUser,
                'Tour': MockTour,
                'Payload': MockPayload,
                'BubblImage': MockBubblImage
            };
            createController = function() {
                $injector.get('$controller')("BubblDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:bubblUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
