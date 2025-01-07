module.exports = {
  setupFiles: ['jest-canvas-mock'],
    moduleNameMapper: {
      '@core/(.*)': '<rootDir>/src/app/core/$1',
    },
    preset: 'jest-preset-angular',
    setupFilesAfterEnv: ['<rootDir>/src/setup.jest.ts'],
    bail: false,
    verbose: false,
    collectCoverage: true,
    coverageDirectory: './coverage/jest',
    coverageReporters: ['lcov', 'text'],
    testPathIgnorePatterns: ['<rootDir>/node_modules/'],
    coveragePathIgnorePatterns: ['<rootDir>/node_modules/'],
    coverageThreshold: {
      global: {
        statements: 80
      },
    },
    roots: [
      "<rootDir>"
    ],
    modulePaths: [
      "<rootDir>"
    ],
    moduleDirectories: [
      "node_modules"
    ],
  };
  