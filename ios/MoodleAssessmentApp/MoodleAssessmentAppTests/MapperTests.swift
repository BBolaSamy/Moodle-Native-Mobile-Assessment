import XCTest
@testable import MoodleAssessmentApp

final class MapperTests: XCTestCase {
    func test_courseMapper_mapsDisplayNameAndProgressAndImage() {
        let dto = CourseDTO(
            id: 10,
            fullname: "Algorithms",
            displayname: "Algorithms 101",
            progress: nil,
            completionprogress: 75,
            courseimage: nil,
            overviewfiles: [OverviewFileDTO(fileurl: "https://example.com/cover.png")]
        )

        let model = CourseMapper.map(dto)

        XCTAssertEqual(model.id, 10)
        XCTAssertEqual(model.name, "Algorithms 101")
        XCTAssertEqual(model.progress, 75)
        XCTAssertEqual(model.imageURL?.absoluteString, "https://example.com/cover.png")
    }

    func test_courseMapper_usesCourseImageWhenOverviewFilesMissing() {
        let dto = CourseDTO(
            id: 11,
            fullname: "Databases",
            displayname: "Databases",
            progress: 90,
            completionprogress: nil,
            courseimage: "https://example.com/course.svg",
            overviewfiles: []
        )

        let model = CourseMapper.map(dto)

        XCTAssertEqual(model.imageURL?.absoluteString, "https://example.com/course.svg")
    }

    func test_gradeMapper_handlesMissingGradeFieldsGracefully() {
        let dto = GradeItemDTO(
            id: 22,
            itemname: "Midterm",
            gradeformatted: nil,
            percentageformatted: nil,
            graderaw: nil,
            grademin: nil,
            grademax: nil
        )

        let model = GradeMapper.map(dto)

        XCTAssertEqual(model.id, 22)
        XCTAssertEqual(model.itemName, "Midterm")
        XCTAssertEqual(model.gradeText, "Not graded")
        XCTAssertNil(model.percentageText)
    }

    func test_gradeMapper_treatsDashAsNotGraded() {
        let dto = GradeItemDTO(
            id: 33,
            itemname: "Assignment",
            gradeformatted: "-",
            percentageformatted: "-",
            graderaw: nil,
            grademin: 0,
            grademax: 100
        )

        let model = GradeMapper.map(dto)
        XCTAssertEqual(model.gradeText, "Not graded")
    }
}
