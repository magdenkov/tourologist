/**
 * Created by rafael on 28/11/2016.
 */

angular.module('tourologistApp').factory('BubblEntity', ["Entity", "geoHelpers" ,"$q", function (Entity, geoHelpers, $q) {

    function BubblEntity() {
    }

    BubblEntity.prototype.id = -1;
    BubblEntity.prototype.name = "";
    BubblEntity.prototype.locations = [];

    BubblEntity.prototype.as = function (entity) {
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

    BubblEntity.prototype.map = function (options) {
        return geoHelpers.staticMapFromLocations(this.locations, options);
    };

    BubblEntity.prototype.latLngArray = function (latitude, longitude) {
        var locations = [];
        this.locations.forEach(function (element) {
            var location = {};
            location[latitude] = element.latitude;
            location[longitude] = element.longitude;
            locations.push(location);
        });
        return locations;
    };

    BubblEntity.prototype.getCenter = function () {
        var latitude = 0;
        var longitude = 0;
        this.locations.forEach(function (element) {
            latitude += element.latitude;
            longitude += element.longitude;
        });
        latitude /= this.locations.length;
        longitude /= this.locations.length;
        return new google.maps.LatLng(latitude, longitude);
    };

    BubblEntity.prototype.toGoogleLatLngArray = function () {
        var locations = [];
        this.locations.forEach(function (location) {
            locations.push(new google.maps.LatLng(location.latitude, location.longitude));
        });
        return locations;
    };

    BubblEntity.prototype.toGooglePolygon = function (options) {
        options = options || {};
        var locations = this.latLngArray('lat', 'lng');
        return new google.maps.Circle({
            paths: locations,
            strokeColor: options.strokeColor || '#FF0000',
            strokeOpacity: options.strokeOpacity || 0.8,
            strokeWeight: options.strokeWeight || 2,
            fillColor: options.fillColor || '#FF0000',
            fillOpacity: options.fillOpacity || 0.35
        });
    };

    return angular.extend(BubblEntity, Entity);
}]);
