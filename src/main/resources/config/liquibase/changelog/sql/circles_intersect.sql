CREATE FUNCTION CIRCLES_INTERSECTS (bubblLat DOUBLE, bubblLon DOUBLE, bubblRadius DOUBLE, lat DOUBLE, lon DOUBLE, radius DOUBLE)
    RETURNS BOOLEAN
    DETERMINISTIC
        BEGIN
	        -- http://stackoverflow.com/questions/3349125/circle-circle-intersection-points
            DECLARE dist DOUBLE;

            IF (bubblLat IS NULL) THEN
                return false;
            END IF;

            IF (bubblLon IS NULL) THEN
                return false;
            END IF;

            IF (bubblRadius IS NULL) THEN
                return false;
            END IF;

            IF (lat IS NULL) THEN
                return false;
            END IF;

            IF (lon IS NULL) THEN
                return false;
            END IF;

            IF (radius IS NULL) THEN
                return false;
            END IF;

            SET dist = CALC_DISTANCE(bubblLat, bubblLon, lat, lon);

            IF (dist > bubblRadius + radius) THEN
               return false; -- the circles are separate.
            END IF;

            IF (dist <= 1.7976931348623157E+308 AND abs(bubblRadius - radius) <= 1.7976931348623157E+308) THEN
               return true; -- the circles are coincident
            END IF;

            IF (dist < abs(bubblRadius - radius)) THEN
            	-- one circle is contained within the other
               return bubblRadius < radius;
            END IF;

            return false;
        END
