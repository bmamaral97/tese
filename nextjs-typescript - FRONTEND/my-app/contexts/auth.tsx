import React, { createContext, useContext, useEffect, useState } from "react";
import Router from "next/router";
import { postLoginData, postPublicData } from "../utils/NetworkUtils";

export interface User {
  username: string;
  roles: string[];
}

export interface Role {
  authority: string;
}

export interface SignInData {
  username: string;
  password: string;
}

export interface SignUpData {
  username: string;
  password: string;
}

export interface AuthContextInterface {
  user: User | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  loading: boolean;
  requestLogin: (data: SignInData) => void;
  logout: () => void;
  signUp: (data: SignUpData) => void;
}

const AuthContext = createContext<AuthContextInterface>({
  user: null,
  isAuthenticated: false,
  isAdmin: false,
  loading: true,
  requestLogin: () => null,
  logout: () => null,
  signUp: () => null,
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const jwt = require("jsonwebtoken");

  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);

  const isAuthenticated = !!user;

  useEffect(() => {
    try {
      if (isTokenValid()) {
        setUser({
          username: decodeUsername(),
          roles: decodeRoles(),
        });
      }
    } catch {
      window.alert("JWT Token has expired!");
      localStorage.removeItem("jwt");
      Router.push("/login");
    } finally {
      setLoading(false);
    }
  }, []);

  async function requestLogin(data: SignInData) {
    const token: string = await loginTest(data);
    if (token != null) {
      //console.log(token)
      localStorage.setItem("jwt", token);
      setUser({
        username: decodeUsername(),
        roles: decodeRoles(),
      });
      Router.push("/");
    } else {
      console.error("Failed to login in to the system.");
    }
  }

  async function loginTest(data: SignInData): Promise<string> {
    return await postLoginData("/api/login", data);
  }

  function logout(): void {
    console.log("Logging out from the system.");
    localStorage.removeItem("jwt");
    Router.reload();
  }

  async function signUp({ username, password }: SignUpData) {
    const url = "/api/signup";

    return await fetch(url, {
      method: "POST",
      body: JSON.stringify({
        id: 0,
        username: username,
        password: password,
      }),
    })
      .then((response) => {
        if (response.ok) {
          console.log("REGISTER OK -> RESPONSE:" + JSON.stringify(response));
          const token: string = response.headers.get("Authorization")!;
          localStorage.setItem("jwt", token);
          setUser({
            username: decodeUsername(),
            roles: decodeRoles(),
          });
          alert("successfully registered user " + username);
          Router.push("/");
        } else {
          console.log(`Error: ${response.status}: ${response.statusText}`);
          window.alert("Erro no registo");
        }
      })
      .catch((err) => {
        console.log(err);
        return null;
      });
  }

  function isTokenValid(): boolean {
    const token = localStorage.getItem("jwt");

    if (token) {
      const t = token.slice(7, token.length);
      const decode = jwt.decode(t);

      const iat = decode.iat;
      const exp = decode.exp;
      const crt = Date.now();

      //console.log("The JWT Token in storage was issued at: " + new Date(iat * 1000).toISOString().slice(0, -5))
      //console.log("The JWT Token in storage will expire at: " + new Date(exp * 1000).toISOString().slice(0, -5))
      //console.log("The current system time is: " + new Date(crt).toISOString().slice(0, -5))

      if (exp * 1000 > crt) {
        return true;
      } else {
        throw new Error("The JWT Token in storage has expired.");
      }
    } else {
      return false;
    }
  }

  function decodeUsername(): string {
    const token = localStorage.getItem("jwt");
    if (token) {
      const t = token.slice(7, token.length);
      const decode = jwt.decode(t);
      return decode.username;
    } else {
      throw new Error("Error decoding username from the JWT Token.");
    }
  }

  function decodeRoles(): string[] {
    const token = localStorage.getItem("jwt");
    if (token) {
      const t = token.slice(7, token.length);
      const decode = jwt.decode(t);
      const roles: string[] = [];
      decode.roles.map((role: Role) => {
        roles.push(role.authority);
        if (role.authority === "ADMIN") {
          setIsAdmin(true);
        }
      });
      return roles;
    } else {
      throw new Error("Error decoding user roles from the JWT Token.");
    }
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated,
        isAdmin,
        loading,
        requestLogin,
        logout,
        signUp,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
