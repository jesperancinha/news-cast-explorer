from flask import Flask

app = Flask(__name__)


@app.route('/')
def stream():
  def generate():
    yield '['
    with open('../java-exercise-fetcher/java-exercise-fetcher.log') as f:
      yield f.readline()
      for line in f:
        yield ',' + line
    yield ']'

  return app.response_class(generate(), mimetype='json')


app.run()
