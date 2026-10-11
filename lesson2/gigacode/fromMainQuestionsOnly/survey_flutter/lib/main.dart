import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(const MiniSurveyApp());
}

class MiniSurveyApp extends StatelessWidget {
  const MiniSurveyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Мини-анкета',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const SurveyPage(),
    );
  }
}

class Question {
  final String id;
  final String type;
  final String label;
  final List<String>? options;
  final bool required;

  Question({
    required this.id,
    required this.type,
    required this.label,
    this.options,
    this.required = false,
  });

  factory Question.fromJson(Map<String, dynamic> json) {
    return Question(
      id: json['id'] as String,
      type: json['type'] as String,
      label: json['label'] as String,
      options: json['options'] != null
          ? List<String>.from(json['options'])
          : null,
      required: json['required'] as bool? ?? false,
    );
  }
}

class SurveyPage extends StatefulWidget {
  const SurveyPage({super.key});

  @override
  State<SurveyPage> createState() => _SurveyPageState();
}

class _SurveyPageState extends State<SurveyPage> {
  List<Question> questions = [];
  Map<String, TextEditingController> controllers = {};
  bool isLoading = true;
  String? errorMessage;
  bool isSubmitted = false;

  @override
  void initState() {
    super.initState();
    loadQuestions();
  }

  Future<void> loadQuestions() async {
    try {
      final response = await http.get(
        Uri.parse('http://localhost:8080/questions'),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        setState(() {
          questions = data.map((q) => Question.fromJson(q)).toList();
          controllers = {
            for (final q in questions)
              q.id: TextEditingController(),
          };
          isLoading = false;
        });
      } else {
        setState(() {
          errorMessage = 'Ошибка загрузки вопросов: ${response.statusCode}';
          isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        errorMessage = 'Ошибка подключения: $e';
        isLoading = false;
      });
    }
  }

  Future<void> submitAnswers() async {
    final answers = <String, String>{};
    for (final q in questions) {
      answers[q.id] = controllers[q.id]!.text;
    }

    try {
      final response = await http.post(
        Uri.parse('http://localhost:8080/answers'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'id': '', 'answers': answers}),
      );

      if (response.statusCode == 200) {
        setState(() {
          isSubmitted = true;
        });
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Ошибка отправки: ${response.statusCode}')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Ошибка подключения: $e')),
      );
    }
  }

  @override
  void dispose() {
    for (final c in controllers.values) {
      c.dispose();
    }
    super.dispose();
  }

  Widget _buildQuestionWidget(Question question) {
    switch (question.type) {
      case 'text':
        return TextFormField(
          controller: controllers[question.id],
          decoration: InputDecoration(
            labelText: question.label,
            suffixText: question.required ? '*' : null,
            border: const OutlineInputBorder(),
          ),
          validator: (v) =>
              question.required && (v?.isEmpty ?? true) ? 'Обязательное поле' : null,
        );
      case 'email':
        return TextFormField(
          controller: controllers[question.id],
          decoration: InputDecoration(
            labelText: question.label,
            suffixText: question.required ? '*' : null,
            border: const OutlineInputBorder(),
            icon: const Icon(Icons.email),
          ),
          keyboardType: TextInputType.emailAddress,
          validator: (v) =>
              question.required && (v?.isEmpty ?? true) ? 'Обязательное поле' : null,
        );
      case 'select':
        return DropdownButtonFormField<String>(
          decoration: InputDecoration(
            labelText: question.label,
            suffixText: question.required ? '*' : null,
            border: const OutlineInputBorder(),
          ),
          items: question.options?.map((opt) => DropdownMenuItem(
            value: opt,
            child: Text(opt),
          )).toList(),
          onChanged: (v) => controllers[question.id]!.text = v ?? '',
          validator: (v) =>
              question.required && (v?.isEmpty ?? true) ? 'Обязательное поле' : null,
        );
      case 'radio':
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(question.label, style: const TextStyle(fontWeight: FontWeight.bold)),
            if (question.required) const Text('*', style: TextStyle(color: Colors.red)),
            const SizedBox(height: 8),
            ...(question.options ?? []).map((opt) => RadioListTile<String>(
              title: Text(opt),
              value: opt,
              groupValue: controllers[question.id]!.text,
              onChanged: (v) => controllers[question.id]!.text = v ?? '',
            )),
          ],
        );
      case 'checkbox':
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(question.label, style: const TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            ...(question.options ?? []).map((opt) => CheckboxListTile(
              title: Text(opt),
              value: controllers[question.id]!.text == opt,
              onChanged: (v) => controllers[question.id]!.text =
                  controllers[question.id]!.text == opt ? '' : opt,
            )),
          ],
        );
      default:
        return TextFormField(
          controller: controllers[question.id],
          decoration: InputDecoration(
            labelText: question.label,
            border: const OutlineInputBorder(),
          ),
        );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isSubmitted) {
      return Scaffold(
        appBar: AppBar(title: const Text('Спасибо!')),
        body: const Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.check_circle, size: 100, color: Colors.green),
              SizedBox(height: 24),
              Text('Спасибо за ваши ответы!', style: TextStyle(fontSize: 24)),
              SizedBox(height: 16),
              Text('Ваши ответы успешно отправлены.', style: TextStyle(fontSize: 16)),
            ],
          ),
        ),
      );
    }

    if (isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (errorMessage != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Мини-анкета')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.error_outline, size: 64, color: Colors.red),
              const SizedBox(height: 16),
              Text(errorMessage!, textAlign: TextAlign.center),
            ],
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Мини-анкета'),
        backgroundColor: Theme.of(context).colorScheme.primary,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ...questions.map((q) => Padding(
                padding: const EdgeInsets.only(bottom: 16.0),
                child: _buildQuestionWidget(q),
              )),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: submitAnswers,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                child: const Text(
                  'Отправить',
                  style: TextStyle(fontSize: 18),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
