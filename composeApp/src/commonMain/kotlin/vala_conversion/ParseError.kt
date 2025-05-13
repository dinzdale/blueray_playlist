/*
 * BluRay - a simple sample implementation of Blu-ray Disc specifications
 * Copyright (C) 2011  Luca Wehrstedt

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package BluRay

import java.lang.Exception

// In Vala, errordomain defines a set of error codes associated with a domain.
// In Kotlin, this is typically represented using custom exception classes.
// We define a base exception for the domain and subclasses for each specific error code.

/**
 * Base exception for parsing errors in the BluRay domain.
 */
class ParseError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {

    /**
     * Represents an error related to invalid input data.
     */
    class InputError(message: String? = null, cause: Throwable? = null) : ParseError(message, cause)

    /**
     * Represents an error related to validation failure.
     */
    class ValidationError(message: String? = null, cause: Throwable? = null) : ParseError(message, cause)
}
