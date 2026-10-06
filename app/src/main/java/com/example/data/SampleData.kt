package com.example.data

object SampleData {
    val initialStudents = listOf(
        Student(
            studentId = "STU1001",
            name = "Alex Rivera",
            rollNumber = "CS-2023-01",
            gender = "Male",
            dob = "2003-05-14",
            department = "Computer Science",
            academicYear = "3rd Year",
            phone = "555-010-4821",
            email = "alex.rivera@university.edu",
            address = "742 Evergreen Terrace, Springfield, OR"
        ),
        Student(
            studentId = "STU1002",
            name = "Sophia Chen",
            rollNumber = "IT-2024-15",
            gender = "Female",
            dob = "2004-11-20",
            department = "Information Technology",
            academicYear = "2nd Year",
            phone = "555-012-7893",
            email = "sophia.chen@university.edu",
            address = "1204 Pine Hollow Way, Seattle, WA"
        ),
        Student(
            studentId = "STU1003",
            name = "Marcus Vance",
            rollNumber = "ECE-2022-08",
            gender = "Male",
            dob = "2002-08-30",
            department = "Electronics",
            academicYear = "Final Year",
            phone = "555-019-3344",
            email = "m.vance@university.edu",
            address = "450 University Ave, Palo Alto, CA"
        ),
        Student(
            studentId = "STU1004",
            name = "Aaliyah Patel",
            rollNumber = "EE-2025-22",
            gender = "Female",
            dob = "2005-02-17",
            department = "Electrical Engineering",
            academicYear = "1st Year",
            phone = "555-014-9988",
            email = "aaliyah.patel@university.edu",
            address = "88 West Cambridge Dr, Austin, TX"
        ),
        Student(
            studentId = "STU1005",
            name = "David Kim",
            rollNumber = "ME-2023-45",
            gender = "Male",
            dob = "2003-09-05",
            department = "Mechanical Engineering",
            academicYear = "3rd Year",
            phone = "555-018-6211",
            email = "david.kim@university.edu",
            address = "310 Michigan Blvd, Chicago, IL"
        ),
        Student(
            studentId = "STU1006",
            name = "Elena Rostova",
            rollNumber = "CE-2022-19",
            gender = "Female",
            dob = "2002-04-12",
            department = "Civil Engineering",
            academicYear = "Final Year",
            phone = "555-013-4412",
            email = "elena.rostova@university.edu",
            address = "670 Ocean View Rd, San Diego, CA"
        ),
        Student(
            studentId = "STU1007",
            name = "Jordan Miller",
            rollNumber = "BA-2024-03",
            gender = "Other",
            dob = "2004-07-25",
            department = "Business Administration",
            academicYear = "2nd Year",
            phone = "555-017-8822",
            email = "jordan.m@university.edu",
            address = "15 Liberty St, New York, NY"
        ),
        Student(
            studentId = "STU1008",
            name = "Priya Sharma",
            rollNumber = "CS-2025-09",
            gender = "Female",
            dob = "2005-12-03",
            department = "Computer Science",
            academicYear = "1st Year",
            phone = "555-011-5533",
            email = "priya.sharma@university.edu",
            address = "520 North Elm St, Boston, MA"
        )
    )

    val departments = listOf(
        "Computer Science",
        "Information Technology",
        "Electronics",
        "Electrical Engineering",
        "Mechanical Engineering",
        "Civil Engineering",
        "Business Administration"
    )

    val academicYears = listOf(
        "1st Year",
        "2nd Year",
        "3rd Year",
        "Final Year"
    )

    val genders = listOf("Male", "Female", "Other")
}
