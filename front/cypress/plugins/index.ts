import { defineConfig } from 'cypress';

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on: any, config: any) {
      require('@cypress/code-coverage/task')(on, config);
      return config;
    },
  },
});
