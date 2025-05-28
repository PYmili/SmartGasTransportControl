// src/components/util/JwtUtil.js
import axios from 'axios';

export const verifyJwtRequest = async (jwt) => {
  if (jwt === null || jwt === undefined || jwt === '') {
    return false;
  }

  try {
    const response = await axios.post("http://localhost:8080/user/verify", {
      jwt: jwt
    });

    if (response.data.code !== 200) {
      return false;
    }

    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
};