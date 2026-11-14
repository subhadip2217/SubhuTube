/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        tube: {
          dark: '#0f0f0f',
          card: '#212121',
          hover: '#303030',
          red: '#ff0000',
          text: '#f1f1f1',
          muted: '#aaaaaa',
        }
      }
    },
  },
  plugins: [],
}