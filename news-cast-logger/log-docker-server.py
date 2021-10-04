from flask import Flask

app = Flask(__name__)

@app.route('/')
def stream():
  def generate():
    yield '['
    try:
      with open('twitter-fetcher.log') as f:
        yield f.readline()
        for line in f:
          yield ',' + line
    finally:
      yield ']'

  return app.response_class(generate(), mimetype='json')

app.run(host='0.0.0.0')
