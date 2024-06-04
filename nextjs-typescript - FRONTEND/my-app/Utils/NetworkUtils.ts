// GET PUBLIC DATA
export async function getPublicData<T>(url: string): Promise<T> {
  console.log(`Fetching Public Data on url: ${process.env.BASE_URL + url}`);

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  return fetch(process.env.BASE_URL + url, {
    method: "GET",
    cache: "no-cache",
    headers: myHeaders,
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          `Success on fetching the data! (${response.status}: ${response.statusText})`
        );
        return response.json();
      } else {
        console.error(
          `Error fetching the data! (${response.status}: ${response.statusText})`
        );
        return new Promise((resolve) => {
          resolve(null);
        });
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
      return new Promise((resolve) => {
        resolve(null);
      });
    });
}

// GET PRIVATE DATA
export async function getPrivateData<T>(url: string): Promise<T> {
  console.log(`Fetching Private Data on url: ${process.env.BASE_URL + url}`);

  const token = localStorage.getItem("jwt");
  if (!token) throw new Error("Invalid token...");

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "" + token);

  return fetch(process.env.BASE_URL + url, {
    method: "GET",
    cache: "no-cache",
    headers: myHeaders,
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          `Success on fetching the private data! (${response.status}: ${response.statusText})`
        );
        return response.json();
      } else {
        console.error(
          `Error fetching the private data! (${response.status}: ${response.statusText})`
        );
        return new Promise((resolve) => {
          resolve(null);
        });
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
      return new Promise((resolve) => {
        resolve(null);
      });
    });
}

// POST PUBLIC DATA
export async function postPublicData<T>(url: string, data: any): Promise<T> {
  console.log(`Posting Public Data on url: ${process.env.BASE_URL + url}`);

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  return fetch(process.env.BASE_URL + url, {
    method: "POST",
    cache: "no-cache",
    headers: myHeaders,
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        console.log(`Success on posting the public data! (${response.status})`);
        return response.json();
      } else {
        console.error(`Error posting the public data! (${response.status})`);
        return new Promise((resolve) => {
          resolve(null);
        });
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
      return new Promise((resolve) => {
        resolve(null);
      });
    });
}

// POST PRIVATE DATA
export async function postPrivateData<T>(url: string, data: any): Promise<T> {
  console.log(`Posting Private Data on url: ${process.env.BASE_URL + url}`);

  const token = localStorage.getItem("jwt");
  if (!token) {
    throw new Error("Invalid token...");
  }

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "" + token);

  return fetch(process.env.BASE_URL + url, {
    method: "POST",
    cache: "no-cache",
    headers: myHeaders,
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          `Success on posting the private data! (${response.status}: ${response.statusText})`
        );
        return response.json();
      } else {
        console.error(
          `Error posting the private data! (${response.status}: ${response.statusText})`
        );
        return new Promise((resolve) => {
          resolve(null);
        });
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
      return new Promise((resolve) => {
        resolve(null);
      });
    });
}

export async function postPrivateDataVoid(url: string, data: any) {
  console.log(`Posting on url: ${process.env.BASE_URL + url}`);

  const token = localStorage.getItem("jwt");
  if (!token) {
    throw new Error("Invalid token...");
  }

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "" + token);

  fetch(process.env.BASE_URL + url, {
    method: "POST",
    cache: "no-cache",
    headers: myHeaders,
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        console.log(`Success on posting data! (${response.status})`);
      } else {
        console.error(`Error posting the data! (${response.status})`);
      }
    })
    .catch((error) => {
      console.error(`Error contacting the server! (${error.status})`);
    });
}

// POST PRIVATE FILE
export async function postPrivateFile(
  url: string,
  data: File,
  instructor: string
) {
  console.log(`Posting Private Data on url: ${process.env.BASE_URL + url}`);

  const token = localStorage.getItem("jwt");
  if (!token) {
    throw new Error("Invalid token...");
  }

  if (data === null) {
    throw new Error("File is null...");
  }

  const formData = new FormData();
  formData.append("file", data);
  formData.append("instructor", instructor);

  const myHeaders = new Headers();
  myHeaders.append("Authorization", "" + token);

  fetch(process.env.BASE_URL + url, {
    method: "POST",
    headers: myHeaders,
    body: formData,
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          `Success on posting the private file! (${response.status})`
        );
      } else {
        console.error(
          `Error posting the private file! (${response.status}: ${response.statusText})`
        );
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
    });
}

// DELETE PRIVATE DATA
export async function deletePrivateData(url: string) {
  console.log(`Deleting Private Data on url: ${process.env.BASE_URL + url}`);

  const token = localStorage.getItem("jwt");
  if (!token) throw new Error("No token...");

  const myHeaders = new Headers();
  myHeaders.append("Authorization", "" + token);

  return fetch(process.env.BASE_URL + url, {
    method: "DELETE",
    headers: myHeaders,
  })
    .then((response) => {
      if (response.ok) {
        console.log(`Success on deleting the data! (${response.status})`);
      } else {
        console.error(`Error deleting the data! (${response.status})`);
      }
    })
    .catch((error) => {
      console.error(`Error contacting the server! (${error.status})`);
      throw new Error("Error contacting the server!");
    });
}

// POST LOGIN
export async function postLoginData(
  url: string,
  data: { username: string; password: string }
): Promise<any> {
  console.log(`Posting Login Data on url: ${process.env.BASE_URL + url}`);

  const myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  return fetch(url, {
    method: "POST",
    headers: myHeaders,
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          `Success on logging in! (${response.status}: ${response.statusText})`
        );
        return new Promise((resolve) => {
          resolve(response.headers.get("Authorization") as string);
        });
      } else {
        console.error(
          `Error logging in! (${response.status}: ${response.statusText})`
        );
        return new Promise((resolve) => {
          resolve(null);
        });
      }
    })
    .catch((error) => {
      console.error(
        `Error contacting the server! (${error.status}: ${error.statusText})`
      );
      return new Promise((resolve) => {
        resolve(null);
      });
    });
}
