module.exports = {
  moduleNameMapper: {
    '^.+.(svg|css|scss)$': 'jest-transform-stub',
  },
  "testEnvironment": "jsdom",
  "setupFilesAfterEnv": [
    "<rootDir>/setuptests.ts"
  ],
  preset: 'jest-preset-angular',
  "testMatch": [
    "**/?(*.)+(spec).[jt]s?(x)"
  ],
}
