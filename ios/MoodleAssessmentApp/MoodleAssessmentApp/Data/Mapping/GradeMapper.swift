import Foundation

enum GradeMapper {
    static func map(_ dto: GradeItemDTO) -> GradeItem {
        let name = dto.itemname ?? "Untitled Item"
        let gradeText = normalizedGradeText(dto.gradeformatted) ?? formattedRawGrade(from: dto) ?? "Not graded"

        return GradeItem(
            id: dto.id,
            itemName: name,
            gradeText: gradeText,
            percentageText: dto.percentageformatted
        )
    }

    private static func formattedRawGrade(from dto: GradeItemDTO) -> String? {
        guard let raw = dto.graderaw else { return nil }

        if dto.grademin != nil, let max = dto.grademax {
            return "\(raw.cleanNumber) / \(max.cleanNumber)"
        }

        return raw.cleanNumber
    }

    private static func normalizedGradeText(_ value: String?) -> String? {
        guard let raw = value?.trimmingCharacters(in: .whitespacesAndNewlines), !raw.isEmpty else {
            return nil
        }

        if raw == "-" {
            return nil
        }

        return raw
    }
}

private extension Double {
    var cleanNumber: String {
        truncatingRemainder(dividingBy: 1) == 0 ? String(format: "%.0f", self) : String(format: "%.2f", self)
    }
}
