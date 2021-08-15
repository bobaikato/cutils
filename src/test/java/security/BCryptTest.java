/*
 *  _________  ____ ______________.___.____       _________
 *  \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 *  /    \  \/|    |   / |    |   |   |    |     \_____  \
 *  \     \___|    |  /  |    |   |   |    |___  /        \
 *   \______  /______/   |____|   |___|_______ \/_______  /
 *          \/                                \/        \/
 *
 * Copyright (c) 2006 Damien Miller <djm@mindrot.org>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package security;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.horde.os.cutils.security.BCrypt;

/**
 * JUnit unit tests for BCrypt routines
 *
 * @author Damien Miller
 * @version 0.2
 */

@DisplayName("Test BCrypt operation.")
public final class BCryptTest {
  String test_vectors[][] = {
      {"",
          "$2a$06$DCq7YPn5Rq63x1Lad4cll.",
          "$2a$06$DCq7YPn5Rq63x1Lad4cll.TV4S6ytwfsfvkgY8jIucDrjc8deX1s."},
      {"",
          "$2a$08$HqWuK6/Ng6sg9gQzbLrgb.",
          "$2a$08$HqWuK6/Ng6sg9gQzbLrgb.Tl.ZHfXLhvt/SgVyWhQqgqcZ7ZuUtye"},
      {"",
          "$2a$10$k1wbIrmNyFAPwPVPSVa/ze",
          "$2a$10$k1wbIrmNyFAPwPVPSVa/zecw2BCEnBwVS2GbrmgzxFUOqW9dk4TCW"},
      {"",
          "$2a$12$k42ZFHFWqBp3vWli.nIn8u",
          "$2a$12$k42ZFHFWqBp3vWli.nIn8uYyIkbvYRvodzbfbK18SSsY.CsIQPlxO"},
      {"a",
          "$2a$06$m0CrhHm10qJ3lXRY.5zDGO",
          "$2a$06$m0CrhHm10qJ3lXRY.5zDGO3rS2KdeeWLuGmsfGlMfOxih58VYVfxe"},
      {"a",
          "$2a$08$cfcvVd2aQ8CMvoMpP2EBfe",
          "$2a$08$cfcvVd2aQ8CMvoMpP2EBfeodLEkkFJ9umNEfPD18.hUF62qqlC/V."},
      {"a",
          "$2a$10$k87L/MF28Q673VKh8/cPi.",
          "$2a$10$k87L/MF28Q673VKh8/cPi.SUl7MU/rWuSiIDDFayrKk/1tBsSQu4u"},
      {"a",
          "$2a$12$8NJH3LsPrANStV6XtBakCe",
          "$2a$12$8NJH3LsPrANStV6XtBakCez0cKHXVxmvxIlcz785vxAIZrihHZpeS"},
      {"abc",
          "$2a$06$If6bvum7DFjUnE9p2uDeDu",
          "$2a$06$If6bvum7DFjUnE9p2uDeDu0YHzrHM6tf.iqN8.yx.jNN1ILEf7h0i"},
      {"abc",
          "$2a$08$Ro0CUfOqk6cXEKf3dyaM7O",
          "$2a$08$Ro0CUfOqk6cXEKf3dyaM7OhSCvnwM9s4wIX9JeLapehKK5YdLxKcm"},
      {"abc",
          "$2a$10$WvvTPHKwdBJ3uk0Z37EMR.",
          "$2a$10$WvvTPHKwdBJ3uk0Z37EMR.hLA2W6N9AEBhEgrAOljy2Ae5MtaSIUi"},
      {"abc",
          "$2a$12$EXRkfkdmXn2gzds2SSitu.",
          "$2a$12$EXRkfkdmXn2gzds2SSitu.MW9.gAVqa9eLS1//RYtYCmB1eLHg.9q"},
      {"abcdefghijklmnopqrstuvwxyz",
          "$2a$06$.rCVZVOThsIa97pEDOxvGu",
          "$2a$06$.rCVZVOThsIa97pEDOxvGuRRgzG64bvtJ0938xuqzv18d3ZpQhstC"},
      {"abcdefghijklmnopqrstuvwxyz",
          "$2a$08$aTsUwsyowQuzRrDqFflhge",
          "$2a$08$aTsUwsyowQuzRrDqFflhgekJ8d9/7Z3GV3UcgvzQW3J5zMyrTvlz."},
      {"abcdefghijklmnopqrstuvwxyz",
          "$2a$10$fVH8e28OQRj9tqiDXs1e1u",
          "$2a$10$fVH8e28OQRj9tqiDXs1e1uxpsjN0c7II7YPKXua2NAKYvM6iQk7dq"},
      {"abcdefghijklmnopqrstuvwxyz",
          "$2a$12$D4G5f18o7aMMfwasBL7Gpu",
          "$2a$12$D4G5f18o7aMMfwasBL7GpuQWuP3pkrZrOAnqP.bmezbMng.QwJ/pG"},
      {"~!@#$%^&*()      ~!@#$%^&*()PNBFRD",
          "$2a$06$fPIsBO8qRqkjj273rfaOI.",
          "$2a$06$fPIsBO8qRqkjj273rfaOI.HtSV9jLDpTbZn782DC6/t7qT67P6FfO"},
      {"~!@#$%^&*()      ~!@#$%^&*()PNBFRD",
          "$2a$08$Eq2r4G/76Wv39MzSX262hu",
          "$2a$08$Eq2r4G/76Wv39MzSX262huzPz612MZiYHVUJe/OcOql2jo4.9UxTW"},
      {"~!@#$%^&*()      ~!@#$%^&*()PNBFRD",
          "$2a$10$LgfYWkbzEvQ4JakH7rOvHe",
          "$2a$10$LgfYWkbzEvQ4JakH7rOvHe0y8pHKF9OaFgwUZ2q7W2FFZmZzJYlfS"},
      {"~!@#$%^&*()      ~!@#$%^&*()PNBFRD",
          "$2a$12$WApznUOJfkEGSmYRfnkrPO",
          "$2a$12$WApznUOJfkEGSmYRfnkrPOr466oFDCaj4b6HY3EXGvfxm43seyhgC"},
  };

  /**
   * Test method for 'BCrypt.hashPassword(String, String)'
   */
  @Test
  public void test_Hash_password() {
    System.out.print("BCrypt.hashPassword(): ");
    for (final String[] test_vector : test_vectors) {
      String plain = test_vector[0];
      String salt = test_vector[1];
      String expected = test_vector[2];
      String hashed = BCrypt.hashPassword(plain, salt);
      assertEquals(hashed, expected);
      System.out.print(".");
    }
    System.out.println("");
  }

  /**
   * Test method for 'BCrypt.generateSalt(int)'
   */
  @Test
  public void test_Generate_Salt_Int() {
    System.out.print("BCrypt.generateSalt(log_rounds):");
    for (int i = 4; i <= 12; i++) {
      System.out.print(" " + Integer.toString(i) + ":");
      for (int j = 0; j < test_vectors.length; j += 4) {
        String plain = test_vectors[j][0];
        String salt = BCrypt.generateSalt(i);
        String hashed1 = BCrypt.hashPassword(plain, salt);
        String hashed2 = BCrypt.hashPassword(plain, hashed1);
        assertEquals(hashed1, hashed2);
        System.out.print(".");
      }
    }
    System.out.println("");
  }

  /**
   * Test method for 'BCrypt.generateSalt()'
   */
  @Test
  public void test_GenerateSalt() {
    System.out.print("BCrypt.generateSalt(): ");
    for (int i = 0; i < test_vectors.length; i += 4) {
      String plain = test_vectors[i][0];
      String salt = BCrypt.generateSalt();
      String hashed1 = BCrypt.hashPassword(plain, salt);
      String hashed2 = BCrypt.hashPassword(plain, hashed1);
      assertEquals(hashed1, hashed2);
      System.out.print(".");
    }
    System.out.println("");
  }

  /**
   * Test method for 'BCrypt.verifyPassword(String, String)'
   * expecting success
   */
  @Test
  public void test_verifyPassword_success() {
    System.out.print("BCrypt.verifyPassword w/ good passwords: ");
    for (int i = 0; i < test_vectors.length; i++) {
      String plain = test_vectors[i][0];
      String expected = test_vectors[i][2];
      Assertions.assertTrue(BCrypt.verifyPassword(plain, expected));
      System.out.print(".");
    }
    System.out.println("");
  }

  /**
   * Test method for 'BCrypt.verifyPassword(String, String)'
   * expecting failure
   */
  @Test
  public void test_Verify_Password_failure() {
    System.out.print("BCrypt.verifyPassword w/ bad passwords: ");
    for (int i = 0; i < test_vectors.length; i++) {
      int broken_index = (i + 4) % test_vectors.length;
      String plain = test_vectors[i][0];
      String expected = test_vectors[broken_index][2];
      Assertions.assertFalse(BCrypt.verifyPassword(plain, expected));
      System.out.print(".");
    }
    System.out.println("");
  }

  /**
   * Test for correct hashing of non-US-ASCII passwords
   */
  @Test
  public void test_International_Chars() {
    System.out.print("BCrypt.hashPassword w/ international chars: ");
    String pw1 = "\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605";
    String pw2 = "????????";

    String h1 = BCrypt.hashPassword(pw1, BCrypt.generateSalt());
    Assertions.assertFalse(BCrypt.verifyPassword(pw2, h1));
    System.out.print(".");

    String h2 = BCrypt.hashPassword(pw2, BCrypt.generateSalt());
    Assertions.assertFalse(BCrypt.verifyPassword(pw1, h2));
    System.out.print(".");
    System.out.println("");
  }

  @Test
  public void test_Invalid_Salt_Round_Value(){
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      BCrypt.generateSalt(31);
    });
  }


  @Test
  public void test_Invalid_Salt_Version(){
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      BCrypt.hashPassword("@Donald", "Trump Salt example");
    });
  }

}
