'use strict';

describe('Controller Tests', function() {

    describe('BubblRating Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBubblRating, MockUser, MockBubbl;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBubblRating = jasmine.createSpy('MockBubblRating');
            MockUser = jasmine.createSpy('MockUser');
            MockBubbl = jasmine.createSpy('MockBubbl');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BubblRating': MockBubblRating,
                'User': MockUser,
                'Bubbl': MockBubbl
            };
            createController = function() {
                $injector.get('$controller')("BubblRatingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:bubblRatingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
