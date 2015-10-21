/*
 * m4ri_wrapper.c
 *
 * Copyright (C) 2015 Brenton Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdio.h>
#include <stdlib.h>
//#include <m4ri/config.h>
#include <m4ri/m4ri.h>
#include "m4rjni_Mzd.h"


/*
 * Class:     Mzd
 * Method:    m4ri_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_Mzd_m4ri_1init(JNIEnv *env, jclass obj) {
  m4ri_init();
}

/*
 * Class:     Mzd
 * Method:    mzd_init
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1init(JNIEnv *env, jobject obj,
                                           jint m, jint n) {

  mzd_t *M = mzd_init(m, n);

  return (long) M;
}

/*
 * Class:     Mzd
 * Method:    mzd_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1destroy(JNIEnv *env, jobject obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  mzd_free(M);
}


/*
 * Class:     Mzd
 * Method:    mzd_copy
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1copy(JNIEnv *env, jobject obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return 0;
  mzd_t* D = mzd_copy(NULL,M);
  return (jlong) D;
}

/*
 * Class:     Mzd
 * Method:    mzd_get_nrows
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1get_1nrows(JNIEnv *env, jclass obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return -1;
  return M->nrows;
}

/*
 * Class:     Mzd
 * Method:    mzd_get_ncols
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1get_1ncols(JNIEnv *env, jclass obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return -1;
  return M->ncols;
}

/*
 * Class:     Mzd
 * Method:    mzd_write_bit
 * Signature: (JIII)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1write_1bit(JNIEnv *env, jobject obj, jlong ptr, jint row, jint col, jint val) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return;
  mzd_write_bit(M, row, col, val);
}

/*
 * Class:     Mzd
 * Method:    mzd_read_bit
 * Signature: (JIII)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1read_1bit(JNIEnv *env, jobject obj, jlong ptr, jint row, jint col) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return -1;
  return mzd_read_bit(M, row, col);
}

/*
 * Class:     Mzd
 * Method:    mzd_echelonize
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1echelonize(JNIEnv *env, jobject obj, jlong ptr, jboolean full) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return -1;
  return mzd_echelonize_naive(M,full);
}

/*
 * Class:     Mzd
 * Method:    mzd_solve_left
 * Signature: (JJIZ)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1solve_1left(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr, jint cutoff, jboolean inconsistency_check) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return -1;
  return mzd_solve_left(A,B,cutoff,inconsistency_check);
}

/*
 * Class:     Mzd
 * Method:    mzd_kernel_left_pluq
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1kernel_1left_1pluq(JNIEnv *env, jobject obj, jlong ptr, jint cutoff) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return 0;
  return (jlong) mzd_kernel_left_pluq(M,cutoff);
}

/*
 * Class:     Mzd
 * Method:    mzd_randomize
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1randomize(JNIEnv *env, jobject obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return;
  mzd_randomize(M);
}

/*
 * Class:     Mzd
 * Method:    mzd_print
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1print(JNIEnv *env, jobject obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return;
  mzd_print(M);
  fflush(stdout);
}

/*
 * Class:     Mzd
 * Method:    mzd_info
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1info(JNIEnv *env, jobject obj, jlong ptr, jboolean do_rank) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return;
  mzd_info(M, do_rank);
  fflush(stdout);
}

/*
 * Class:     Mzd
 * Method:    mzd_concat
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1concat(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return -1;
  return (jlong)mzd_concat(A,B,NULL);
}

/*
 * Class:     Mzd
 * Method:    mzd_concat
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1stack(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return -1;
  //printf("about to call mzd_stack()\n\n");
  return (jlong)mzd_stack(NULL,A,B);
}

/*
 * Class:     Mzd
 * Method:    mzd_is_zero
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1is_1zero(JNIEnv *env, jobject obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return -1;
  return mzd_is_zero(M);
}

/*
 * Class:     Mzd
 * Method:    mzd_mul_naive
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1mul_1naive(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return 0;
  return (jlong)mzd_mul_naive(NULL,A,B);
}

/*
 * Class:     Mzd
 * Method:    mzd_transpose
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1transpose(JNIEnv *env, jclass obj, jlong ptr) {
  mzd_t *M = (mzd_t*)ptr;
  if (M==NULL) return 0;
  return (jlong)mzd_transpose(NULL, M);
}

/*
 * Class:     Mzd
 * Method:    mzd_equal
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_Mzd_mzd_1equal(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return 0;
  return mzd_equal(A,B);
}

/*
 * Class:     Mzd
 * Method:    mzd_equal
 * Signature: (JJ)I
 */
JNIEXPORT jlong JNICALL Java_Mzd_mzd_1add(JNIEnv *env, jclass obj, jlong Aptr, jlong Bptr) {
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return 0;
  return (jlong)mzd_add(NULL,A,B);
}


/*
 * NOTE: the order of the arguments is reversed in the m4ri documentation!!!
 *
 * Class:     Mzd
 * Method:    mzd_copy_row
 * Signature: (JIJI)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1copy_1row(JNIEnv *env, jclass obj, jlong Bptr, jint i, jlong Aptr, jint j) {
  //printf("Java_Mzd_mzd_1copy_1row(%d,%d)\n",i,j);
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return;
  if (i<0 || j<0 || i>=B->nrows || j>=A->nrows) {
    printf("ERROR: Java_Mzd_mzd_1copy_1row() - invalid indices passed in: (%d,%d)\n",i,j);
    return;
  }
  //printf("calling mzd_copy_row()!!\n");
  mzd_copy_row(B,i,A,j);
}


/*
 * Class:     Mzd
 * Method:    mzd_copy_rows
 * Signature: (JIJII)V
 */
JNIEXPORT void JNICALL Java_Mzd_mzd_1copy_1rows(JNIEnv *env, jclass obj, jlong Bptr, jint i, jlong Aptr, jint j, jint n) {
  int idx = 0;
  mzd_t *A = (mzd_t*)Aptr;
  mzd_t *B = (mzd_t*)Bptr;
  if (A==NULL || B==NULL) return;

  if (i<0 || j<0 || (i+n)>B->nrows || (j+n)>A->nrows) {
    printf("ERROR: Java_Mzd_mzd_1copy_1rows() - invalid indices passed in\n");
    return;
  }

  for (idx=0; idx<n; idx++) {
    mzd_copy_row(B,i+idx,A,j+idx);
  }
}


/*
 * Class:     Mzd
 * Method:    m4ri_srandom
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_Mzd_m4ri_1srandom(JNIEnv *env, jclass obj, jint seed) {
  srandom(seed);
}


/*
 * Class:     Mzd
 * Method:    m4ri_row_add
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_Mzd_m4ri_1row_1add(JNIEnv *env, jobject obj, jlong Mptr, jint srcRow, jint dstRow) {
  mzd_t *M = (mzd_t*)Mptr;
  if (M==NULL) return;
  if (srcRow<0 || dstRow<0 || srcRow>=M->nrows || dstRow>=M->nrows) {
    printf("ERROR: Java_Mzd_m4ri_1row_1add() - invalid indices passed in\n");
    return;
  }
  mzd_row_add(M, srcRow, dstRow);
}


/*
 * Class:     Mzd
 * Method:    m4ri_row_clear_offset
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_Mzd_m4ri_1row_1clear_1offset(JNIEnv *env, jobject obj, jlong Mptr, jint row, jint colOffset) {
  mzd_t *M = (mzd_t*)Mptr;
  if (M==NULL) return;
  if (row<0 || colOffset<0 || row>=M->nrows || colOffset>=M->ncols) {
    printf("ERROR: Java_Mzd_m4ri_1row_1clear_1offset() - invalid indices passed in\n");
    return;
  }
  mzd_row_clear_offset(M, row, colOffset);
}


/*
 * Class:     Mzd
 * Method:    m4ri_submatrix
 * Signature: (JJIIII)J
 */
JNIEXPORT jlong JNICALL Java_Mzd_m4ri_1submatrix(JNIEnv *env, jobject obj, jlong Sptr, jlong Mptr, jint lowr, jint lowc, jint highr, jint highc) {
  mzd_t *S = NULL;
  mzd_t *M = (mzd_t*)Mptr;
  if (lowr<0 || lowc<0 || lowr>=M->nrows || lowc>=M->nrows) {
    printf("ERROR: Java_Mzd_m4ri_1submatrix() - invalid indices passed in\n");
    return 0;
  }

  if (Sptr != 0) {
    if ((highr-lowr)<0 || (highr-lowr)>=S->nrows || (highc-lowc)<0 || (highc-lowc)>=S->ncols) {
      printf("ERROR: Java_Mzd_m4ri_1submatrix() - invalid indices passed in\n");
      return 0;
    }
    S = (mzd_t*)Sptr;
  }

  //printf("about to call mzd_submatrix()\n\n");
  return (jlong) mzd_submatrix(S, M, lowr, lowc, highr, highc);
}


