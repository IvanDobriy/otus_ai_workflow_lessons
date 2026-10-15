import 'package:flutter/material.dart';
import 'package:survey_api/api.dart';

void main() {
  runApp(const MiniSurveyApp());
}

class MiniSurveyApp extends StatelessWidget {
  const MiniSurveyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Мини-анкета',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorSchemeSeed: Colors.indigo,
        useMaterial3: true,
      ),
      home: const SurveyPage(),
    );
  }
}

class SurveyPage extends StatefulWidget {
  const SurveyPage({super.key});

  @override
  State<SurveyPage> createState() => _SurveyPageState();
}

class _SurveyPageState extends State<SurveyPage> {
  late final SurveyApi _api;

  final Map<String, TextEditingController> _textControllers = {};
  final Map<String, String?> _selectedOptions = {};

  List<Question>? _questions;
  String? _error;
  bool _loading = true;
  bool _submitting = false;
  bool _thanks = false;

  @override
  void initState() {
    super.initState();
    _api = SurveyApi(ApiClient(basePath: _resolveBasePath()));
    _loadQuestions();
  }

  String _resolveBasePath() {
    final base = Uri.base;
    if (base.scheme == 'http' || base.scheme == 'https') {
      return base.origin;
    }
    return 'http://localhost:8080';
  }

  @override
  void dispose() {
    for (final controller in _textControllers.values) {
      controller.dispose();
    }
    super.dispose();
  }

  Future<void> _loadQuestions() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final questions = await _api.getQuestions() ?? <Question>[];
      if (!mounted) return;
      setState(() {
        _questions = questions;
        for (final question in questions) {
          if (question.type == QuestionType.TEXT) {
            _textControllers[question.id] = TextEditingController();
          } else {
            _selectedOptions[question.id] = null;
          }
        }
        _loading = false;
      });
    } catch (error) {
      if (!mounted) return;
      setState(() {
        _error = 'Не удалось загрузить вопросы: $error';
        _loading = false;
      });
    }
  }

  List<Answer> _collectAnswers() {
    final answers = <Answer>[];
    for (final question in _questions ?? <Question>[]) {
      if (question.type == QuestionType.TEXT) {
        answers.add(
          Answer(
            questionId: question.id,
            textValue: _textControllers[question.id]?.text.trim() ?? '',
          ),
        );
      } else {
        answers.add(
          Answer(
            questionId: question.id,
            selectedOptionId: _selectedOptions[question.id],
          ),
        );
      }
    }
    return answers;
  }

  Future<void> _submit() async {
    setState(() {
      _submitting = true;
      _error = null;
    });
    try {
      final result = await _api.submitAnswers(
        AnswerSubmission(answers: _collectAnswers()),
      );
      if (!mounted) return;
      setState(() {
        _submitting = false;
        _thanks = result?.accepted ?? true;
      });
    } catch (error) {
      if (!mounted) return;
      setState(() {
        _submitting = false;
        _error = 'Не удалось отправить ответы: $error';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Мини-анкета'),
        centerTitle: true,
      ),
      body: Center(child: _buildBody(context)),
    );
  }

  Widget _buildBody(BuildContext context) {
    if (_loading) {
      return const CircularProgressIndicator();
    }
    if (_thanks) {
      return _buildThanks(context);
    }
    if (_error != null && _questions == null) {
      return _buildError(context, _error!);
    }
    return _buildForm(context);
  }

  Widget _buildThanks(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        const Icon(Icons.check_circle_outline, size: 96, color: Colors.green),
        const SizedBox(height: 16),
        Text(
          'Спасибо!',
          style: Theme.of(context).textTheme.headlineMedium,
        ),
        const SizedBox(height: 8),
        const Text('Ваши ответы приняты.'),
        const SizedBox(height: 24),
        FilledButton.tonal(
          onPressed: () => setState(() {
            _thanks = false;
            _error = null;
            _questions = null;
            _textControllers.clear();
            _selectedOptions.clear();
            _loadQuestions();
          }),
          child: const Text('Пройти ещё раз'),
        ),
      ],
    );
  }

  Widget _buildError(BuildContext context, String message) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        const Icon(Icons.error_outline, size: 64, color: Colors.redAccent),
        const SizedBox(height: 16),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24),
          child: Text(message, textAlign: TextAlign.center),
        ),
        const SizedBox(height: 16),
        FilledButton(onPressed: _loadQuestions, child: const Text('Повторить')),
      ],
    );
  }

  Widget _buildForm(BuildContext context) {
    final questions = _questions ?? <Question>[];
    return ConstrainedBox(
      constraints: const BoxConstraints(maxWidth: 640),
      child: ListView(
        padding: const EdgeInsets.all(24),
        shrinkWrap: true,
        children: [
          Text(
            'Ответьте, пожалуйста, на несколько вопросов',
            style: Theme.of(context).textTheme.titleLarge,
          ),
          const SizedBox(height: 8),
          const Divider(),
          for (final question in questions) _buildQuestion(context, question),
          if (_error != null) ...[
            const SizedBox(height: 8),
            Text(_error!, style: const TextStyle(color: Colors.red)),
          ],
          const SizedBox(height: 24),
          FilledButton(
            onPressed: _submitting ? null : _submit,
            child: _submitting
                ? const SizedBox(
                    height: 20,
                    width: 20,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Text('Отправить'),
          ),
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  Widget _buildQuestion(BuildContext context, Question question) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            question.text,
            style: Theme.of(context).textTheme.titleMedium,
          ),
          const SizedBox(height: 8),
          if (question.type == QuestionType.TEXT)
            TextField(
              controller: _textControllers[question.id],
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: 'Ваш ответ',
              ),
              maxLines: 1,
            )
          else
            ...question.options.map(
              (option) => _buildOptionTile(context, question, option),
            ),
        ],
      ),
    );
  }

  Widget _buildOptionTile(
    BuildContext context,
    Question question,
    QuestionOption option,
  ) {
    final selected = _selectedOptions[question.id] == option.id;
    return Card(
      elevation: 0,
      color: selected
          ? Theme.of(context).colorScheme.primaryContainer
          : Theme.of(context).colorScheme.surfaceContainerHighest,
      child: ListTile(
        leading: Icon(
          selected ? Icons.radio_button_checked : Icons.radio_button_unchecked,
          color: selected ? Theme.of(context).colorScheme.primary : null,
        ),
        title: Text(option.label),
        onTap: () => setState(() => _selectedOptions[question.id] = option.id),
      ),
    );
  }
}
