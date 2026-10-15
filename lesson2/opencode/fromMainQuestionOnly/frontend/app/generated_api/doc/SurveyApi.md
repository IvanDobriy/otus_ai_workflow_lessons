# survey_api.api.SurveyApi

## Load the API package
```dart
import 'package:survey_api/api.dart';
```

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getQuestions**](SurveyApi.md#getquestions) | **GET** /questions | Получить список вопросов мини-анкеты
[**submitAnswers**](SurveyApi.md#submitanswers) | **POST** /answers | Принять ответы респондента


# **getQuestions**
> List<Question> getQuestions()

Получить список вопросов мини-анкеты

### Example
```dart
import 'package:survey_api/api.dart';

final api_instance = SurveyApi();

try {
    final result = api_instance.getQuestions();
    print(result);
} catch (e) {
    print('Exception when calling SurveyApi->getQuestions: $e\n');
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List<Question>**](Question.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **submitAnswers**
> SubmissionResult submitAnswers(answerSubmission)

Принять ответы респондента

### Example
```dart
import 'package:survey_api/api.dart';

final api_instance = SurveyApi();
final answerSubmission = AnswerSubmission(); // AnswerSubmission | 

try {
    final result = api_instance.submitAnswers(answerSubmission);
    print(result);
} catch (e) {
    print('Exception when calling SurveyApi->submitAnswers: $e\n');
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **answerSubmission** | [**AnswerSubmission**](AnswerSubmission.md)|  | 

### Return type

[**SubmissionResult**](SubmissionResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

