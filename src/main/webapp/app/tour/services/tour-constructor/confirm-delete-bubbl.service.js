(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('ConfirmDeleteBubblTourConstructorService', ['$q', 'ModalService', ConfirmDeleteBubblTourConstructorService]);

    function ConfirmDeleteBubblTourConstructorService($q, modal) {
        var service = this;


        service.call = function (bubbl) {
            var deferred = $q.defer();

            modal.open('app/tour/services/tour-constructor/confirm-delete-bubbl.modal.html',
                {
                    title: bubbl.name
                }
            ).then(
                function () {
                    deferred.resolve(true);
                }, function () {
                    deferred.resolve(false);
                });

            return deferred.promise;
        };
    }

})();

