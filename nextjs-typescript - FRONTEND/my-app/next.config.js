/** @type {import('next').NextConfig} */
//const nextConfig = {
//  reactStrictMode: true,
//}

//module.exports = nextConfig


module.exports = {
  async rewrites() {
    return [
      {
        source: '/api/:slug*',
        destination: 'http://localhost:8080/api/:slug*'
      },
    ]
  },  reactStrictMode: false,
  env: {
    BASE_URL: process.env.BASE_URL,
  },
}