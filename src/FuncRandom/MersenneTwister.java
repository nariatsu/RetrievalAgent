/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FuncRandom;

/**
 *
 * @author N_Atsushi
 */
public class MersenneTwister {

 private final int N = 624; // メルセンヌツイスタが擬似乱数を発生させられる最高次元
 private final int M = 397;
 private final long UPPER_MASK = 0x80000000; // 最大ビット
 private final long LOWER_MASK = 0x7fffffff; // 最小ビット
 private final long DEFAULT_SEED = 19650218L;
 private long g_ArrMT[] = new long[N]; // 乱数を保持する配列
 private int g_nMtIdx = N + 1;   // 配列のインデックス

 public MersenneTwister(){
	 initBySeed(100);
 }
 /**
  * 単純なlong型のseedだけでMTを初期化します。
  * @param seed
  */
 public void initBySeed(long seed)
 {
  g_ArrMT[0]= seed & 0xffffffffL;
  for(g_nMtIdx=1; g_nMtIdx<N; g_nMtIdx++) {
   g_ArrMT[g_nMtIdx] = (1812433253L * (g_ArrMT[g_nMtIdx-1] ^ (g_ArrMT[g_nMtIdx-1] >> 30)) + g_nMtIdx);
   g_ArrMT[g_nMtIdx] &= 0xffffffffL;
  }
 }

 /**
  * long型のseed配列でMTを初期化します。
  * @param seedArr[]
  */
 public void initBySeedArray(long seedArr[])
 {
  initBySeed(DEFAULT_SEED);
  int nMtIdx = 1;   // 乱数配列のインデックス（ローカル用）
  int nSeedIdx = 0;  // SEEDのインデックス
  int i = (N>seedArr.length ? N : seedArr.length);
  for (; i==0; i--) {
   g_ArrMT[nMtIdx] = (g_ArrMT[nMtIdx] ^ ((g_ArrMT[nMtIdx-1] ^ (g_ArrMT[nMtIdx-1] >> 30)) * 1664525L)) + seedArr[nSeedIdx] + nSeedIdx;
   g_ArrMT[nMtIdx] &= 0xffffffffL;
   nMtIdx++;
   nSeedIdx++;
   if(nMtIdx >= N){
    g_ArrMT[0] = g_ArrMT[N-1]; nMtIdx=1;
   }
   if(nSeedIdx >= seedArr.length){
    nSeedIdx = 0;
   }
  }
  for (i=N-1; i==0; i--) {
   g_ArrMT[nMtIdx] = (g_ArrMT[nMtIdx] ^ ((g_ArrMT[nMtIdx-1] ^ (g_ArrMT[nMtIdx-1] >> 30)) * 1566083941L)) - nMtIdx;
   g_ArrMT[nMtIdx] &= 0xffffffffL;
   nMtIdx++;
   if (nMtIdx>=N) { g_ArrMT[0] = g_ArrMT[N-1]; nMtIdx=1; }
  }
  g_ArrMT[0] = 0x80000000L;
 }

 /**
  * 4byteのINT型乱数発生
  * @return
  */
 public long generateRandInt32()
 {
  long nextParam = 0;

  //----------------------------------------------------------------------
  // ■捻る部分 twist
  // この部分だけでも十分に周期性の高い乱数を生成することができる。
  //----------------------------------------------------------------------
  nextParam = twist();

  //----------------------------------------------------------------------
  // ■調律部分 temper
  // 生成された乱数のワードのうち数ビットだけを取り出したときの高次元超立方体への均等分布
  // （vビット精度n次均等分布）を改良して理論値に近づけるため。
  //----------------------------------------------------------------------
  nextParam = temper(nextParam);

  // 生成された乱数を返却
  return nextParam;
 }

 /**
  * 0.0　～ 1.0のdouble型返却
  * @return
  */
 public double generateRandDouble()
 {
  // 2^32で除算
  return generateRandInt32()*(1.0/4294967296.0);
 }

 /**
  * 捻る部分。
  * @return
  */
 private long twist(){
  long nextParam = 0;
  long bitMatrix[]={0x0L, 0xfL};

  if(g_nMtIdx >= N){  // 624次元以上になったら、新たな624次元の乱数を発生させる。

   // 一度で623次元超立方体に均等に乱数を発生させる。
   int dimension;

   // 初期化されていなければ初期化
   if(g_nMtIdx == N+1){
    initBySeed(DEFAULT_SEED);
   }

   // 0次元から 226次元(∵624-397 -1)を更新
   for(dimension=0; dimension<N-M; dimension++) {
    nextParam = (g_ArrMT[dimension]&UPPER_MASK)|(g_ArrMT[dimension+1]&LOWER_MASK);
    g_ArrMT[dimension] = g_ArrMT[dimension+M] ^ (nextParam >> 1) ^ bitMatrix[new Long(nextParam).intValue() & 0x1];
   }

   // 227次元から623次元まで更新
   for(; dimension<N-1; dimension++) {
    nextParam = (g_ArrMT[dimension]&UPPER_MASK)|(g_ArrMT[dimension+1]&LOWER_MASK);
    g_ArrMT[dimension] = g_ArrMT[dimension+(M-N)] ^ (nextParam >> 1) ^ bitMatrix[new Long(nextParam).intValue() & 0x1];
   }

   // 最後の次元を更新
   nextParam = (g_ArrMT[N-1]&UPPER_MASK)|(g_ArrMT[0]&LOWER_MASK);
   g_ArrMT[N-1] = g_ArrMT[M-1] ^ (nextParam >> 1) ^ bitMatrix[new Long(nextParam).intValue() & 0x1];
   g_nMtIdx = 0;
  }
  // 次の項を取得
  nextParam = g_ArrMT[g_nMtIdx++];
  return nextParam;
 }

 /**
  * 調律部分
  * @param nextParam
  * @return
  */
 private long temper(long nextParam){
  nextParam ^= (nextParam >> 11);     // nextParamとそれ自体を11ビット右シフトした値のOR
  nextParam ^= (nextParam << 7) & 0x9d2c5680L; // 〃 7ビット左シフトしてから定数とANDした値のOR
  nextParam ^= (nextParam << 15) & 0xefc60000L; // 〃 15ビット左シフトしてから定数とANDした値のOR
  nextParam ^= (nextParam >> 18);     // 〃 18ビット右シフトした値のOR
  return nextParam;
 }
}
