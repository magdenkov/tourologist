/**
 * Created by rafael on 28/11/2016.
 */

angular.module('tourologistApp').factory('API', ["$q", function ($q) {

    function API() {

    }

    API.prototype.as = function (entity) {
        var deferred = $q.defer();
        var self = this;
        this.http.then(function (data) {
            var arr = [];
            if (data.data instanceof Array) {
                data.data.forEach(function (object) {
                    arr.push(entity.build(object));
                });
                deferred.resolve(arr);
                return;
            }
            if (self.singularEntity) {
                deferred.resolve(entity.build(data.data));
                return;
            }
            deferred.resolve([entity.build(data.data)]);
        }, function (error) {
            deferred.reject(error);
        });
        return deferred.promise;
    };

    return new API();
}]);
