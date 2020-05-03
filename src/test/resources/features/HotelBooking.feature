Feature: Booking feature

  Scenario: A room cannot be booked on an un-existing hotel
    Given the employee <123> from the company "Acme"
    When the employee books the room type "Single" in the hotel "Unknown" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "UNKNOWN_HOTEL"

  Scenario: A room of a certain type cannot be booked on a hotel that does not provide it
    Given the employee <123> from the company "Acme"
    And the hotel "Taj Mahal" providing the following rooms
    | Double | 1 |
    When the employee books the room type "Single" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "UNAVAILABLE_ROOM_TYPE"

  Scenario: A room of a certain type can be booked on a hotel when it provides it and is available on the given dates
    Given the employee <123> from the company "Acme"
    And the hotel "Taj Mahal" providing the following rooms
      | Double | 2 |
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking success
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking success
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "NO_MORE_ROOMS_AVAILABLE_ON_GIVEN_DATES"
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-16" to "2020-04-17"
    Then booking success

  Scenario: A change in quantity of rooms should not not affect existing bookings. They will only affect new bookings, made after the change.
    Given the employee <123> from the company "Acme"
    And the hotel "Taj Mahal" providing the following rooms
      | Double | 1 |
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking success
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking failure of type "NO_MORE_ROOMS_AVAILABLE_ON_GIVEN_DATES"
    And the hotel "Taj Mahal" providing the following rooms
      | Double | 2 |
    When the employee books the room type "Double" in the hotel "Taj Mahal" on the dates "2020-04-15" to "2020-04-16"
    Then booking success