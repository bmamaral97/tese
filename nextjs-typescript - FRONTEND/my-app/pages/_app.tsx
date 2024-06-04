import type { AppProps } from "next/app";
import Layout from "../components/Layout";
import { AuthProvider } from "../contexts/auth";
import { ChakraProvider } from "@chakra-ui/react";

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <ChakraProvider>
      <AuthProvider>
        <Layout>
          <Component {...pageProps} />
        </Layout>
      </AuthProvider>
    </ChakraProvider>
  );
}

export default MyApp;
