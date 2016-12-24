CREATE FUNCTION IN_RADIUS (lat1 DOUBLE, lon1 DOUBLE, lat2 DOUBLE, lon2 DOUBLE, radius DOUBLE)
    RETURNS BOOLEAN
    DETERMINISTIC
        BEGIN
            DECLARE dist DOUBLE;
            IF(lat1 IS NULL) THEN
                return false;
            END IF;

            IF(lon1 IS NULL) THEN
                return false;
            END IF;

            IF(lat2 IS NULL) THEN
                return false;
            END IF;

            IF(lon2 IS NULL) THEN
                return false;
            END IF;

            IF(radius IS NULL) THEN
                return false;
            END IF;


            SET dist =  CALC_DISTANCE(lat1, lon1, lat2, lon2);
            RETURN dist < radius;
        END
