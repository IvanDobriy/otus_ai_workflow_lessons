//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//
// @dart=2.18

// ignore_for_file: unused_element, unused_import
// ignore_for_file: always_put_required_named_parameters_first
// ignore_for_file: constant_identifier_names
// ignore_for_file: lines_longer_than_80_chars

part of openapi.api;

/// Тип вопроса анкеты
class QuestionType {
  /// Instantiate a new enum with the provided [value].
  const QuestionType._(this.value);

  /// The underlying value of this enum member.
  final String value;

  @override
  String toString() => value;

  String toJson() => value;

  static const TEXT = QuestionType._(r'TEXT');
  static const SINGLE_CHOICE = QuestionType._(r'SINGLE_CHOICE');

  /// List of all possible values in this [enum][QuestionType].
  static const values = <QuestionType>[
    TEXT,
    SINGLE_CHOICE,
  ];

  static QuestionType? fromJson(dynamic value) => QuestionTypeTypeTransformer().decode(value);

  static List<QuestionType> listFromJson(dynamic json, {bool growable = false,}) {
    final result = <QuestionType>[];
    if (json is List && json.isNotEmpty) {
      for (final row in json) {
        final value = QuestionType.fromJson(row);
        if (value != null) {
          result.add(value);
        }
      }
    }
    return result.toList(growable: growable);
  }
}

/// Transformation class that can [encode] an instance of [QuestionType] to String,
/// and [decode] dynamic data back to [QuestionType].
class QuestionTypeTypeTransformer {
  factory QuestionTypeTypeTransformer() => _instance ??= const QuestionTypeTypeTransformer._();

  const QuestionTypeTypeTransformer._();

  String encode(QuestionType data) => data.value;

  /// Decodes a [dynamic value][data] to a QuestionType.
  ///
  /// If [allowNull] is true and the [dynamic value][data] cannot be decoded successfully,
  /// then null is returned. However, if [allowNull] is false and the [dynamic value][data]
  /// cannot be decoded successfully, then an [UnimplementedError] is thrown.
  ///
  /// The [allowNull] is very handy when an API changes and a new enum value is added or removed,
  /// and users are still using an old app with the old code.
  QuestionType? decode(dynamic data, {bool allowNull = true}) {
    if (data != null) {
      switch (data) {
        case r'TEXT': return QuestionType.TEXT;
        case r'SINGLE_CHOICE': return QuestionType.SINGLE_CHOICE;
        default:
          if (!allowNull) {
            throw ArgumentError('Unknown enum value to decode: $data');
          }
      }
    }
    return null;
  }

  /// Singleton [QuestionTypeTypeTransformer] instance.
  static QuestionTypeTypeTransformer? _instance;
}

