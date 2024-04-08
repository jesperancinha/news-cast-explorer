const path = require('path');

module.exports = {
  resolve: {
    fallback: {
      "crypto": require.resolve("crypto-browserify")
    }
  }
};
