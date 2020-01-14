from time import sleep
from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def stream():
  def generate():
    with open('../java-exercise-fetcher/java-exercise-fetcher.log') as f:
      yield f.read()
      sleep(1)

  return app.response_class(generate(), mimetype='text/plain')

app.run()
