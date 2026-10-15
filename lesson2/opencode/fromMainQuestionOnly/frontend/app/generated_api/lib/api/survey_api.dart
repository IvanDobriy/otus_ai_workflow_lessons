//
// AUTO-GENERATED FILE, DO NOT MODIFY!
//
// @dart=2.18

// ignore_for_file: unused_element, unused_import
// ignore_for_file: always_put_required_named_parameters_first
// ignore_for_file: constant_identifier_names
// ignore_for_file: lines_longer_than_80_chars

part of openapi.api;


class SurveyApi {
  SurveyApi([ApiClient? apiClient]) : apiClient = apiClient ?? defaultApiClient;

  final ApiClient apiClient;

  /// Получить список вопросов мини-анкеты
  ///
  /// Note: This method returns the HTTP [Response].
  Future<Response> getQuestionsWithHttpInfo() async {
    // ignore: prefer_const_declarations
    final path = r'/questions';

    // ignore: prefer_final_locals
    Object? postBody;

    final queryParams = <QueryParam>[];
    final headerParams = <String, String>{};
    final formParams = <String, String>{};

    const contentTypes = <String>[];


    return apiClient.invokeAPI(
      path,
      'GET',
      queryParams,
      postBody,
      headerParams,
      formParams,
      contentTypes.isEmpty ? null : contentTypes.first,
    );
  }

  /// Получить список вопросов мини-анкеты
  Future<List<Question>?> getQuestions() async {
    final response = await getQuestionsWithHttpInfo();
    if (response.statusCode >= HttpStatus.badRequest) {
      throw ApiException(response.statusCode, await _decodeBodyBytes(response));
    }
    // When a remote server returns no body with a status of 204, we shall not decode it.
    // At the time of writing this, `dart:convert` will throw an "Unexpected end of input"
    // FormatException when trying to decode an empty string.
    if (response.body.isNotEmpty && response.statusCode != HttpStatus.noContent) {
      final responseBody = await _decodeBodyBytes(response);
      return (await apiClient.deserializeAsync(responseBody, 'List<Question>') as List)
        .cast<Question>()
        .toList(growable: false);

    }
    return null;
  }

  /// Принять ответы респондента
  ///
  /// Note: This method returns the HTTP [Response].
  ///
  /// Parameters:
  ///
  /// * [AnswerSubmission] answerSubmission (required):
  Future<Response> submitAnswersWithHttpInfo(AnswerSubmission answerSubmission,) async {
    // ignore: prefer_const_declarations
    final path = r'/answers';

    // ignore: prefer_final_locals
    Object? postBody = answerSubmission;

    final queryParams = <QueryParam>[];
    final headerParams = <String, String>{};
    final formParams = <String, String>{};

    const contentTypes = <String>['application/json'];


    return apiClient.invokeAPI(
      path,
      'POST',
      queryParams,
      postBody,
      headerParams,
      formParams,
      contentTypes.isEmpty ? null : contentTypes.first,
    );
  }

  /// Принять ответы респондента
  ///
  /// Parameters:
  ///
  /// * [AnswerSubmission] answerSubmission (required):
  Future<SubmissionResult?> submitAnswers(AnswerSubmission answerSubmission,) async {
    final response = await submitAnswersWithHttpInfo(answerSubmission,);
    if (response.statusCode >= HttpStatus.badRequest) {
      throw ApiException(response.statusCode, await _decodeBodyBytes(response));
    }
    // When a remote server returns no body with a status of 204, we shall not decode it.
    // At the time of writing this, `dart:convert` will throw an "Unexpected end of input"
    // FormatException when trying to decode an empty string.
    if (response.body.isNotEmpty && response.statusCode != HttpStatus.noContent) {
      return await apiClient.deserializeAsync(await _decodeBodyBytes(response), 'SubmissionResult',) as SubmissionResult;
    
    }
    return null;
  }
}
