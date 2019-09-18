package com.rongji.dfish.base.crypt;

import java.math.BigInteger;
import java.util.Random;
/**
 * <p>Title: 榕基I-TASK执行先锋</p>
 *
 * <p>Description: 专门为提高企业执行力而设计的产品</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 榕基软件开发有限公司</p>
 *
 * @author not attributable
 * @version 1.0
 */
public final class RSAKeyGenerator {
  /**
   * 首先, 找出三个数, p, q, r,
   * 其中 p, q 是两个相异的质数, r 是与 (p-1)(q-1) 互质的数......
   * p, q, r 这三个数便是 private key
   *
   * 接著, 找出 m, 使得 rm == 1 mod (p-1)(q-1).....
   * 这个 m 一定存在, 因为 r 与 (p-1)(q-1) 互质, 用辗转相除法就可以得到了.....
   * 再来, 计算 n = pq.......
   * m, n 这两个数便是 public key
   */
  private BigInteger seedP;
  private BigInteger seedQ;
  private BigInteger privateKey;
  private BigInteger publicKey;
  private BigInteger modulus;
  public void setSeedP(BigInteger seedP) {
    this.seedP = seedP;
  }

  public void setSeedQ(BigInteger seedQ) {
    this.seedQ = seedQ;
  }

  public void choosePublicKey(BigInteger publicKey) {
    this.publicKey = publicKey;
  }

  public void generate() throws Exception {
    generate(512, 32);
  }

  public void generate(int modulusBitLen) throws Exception {
    generate(modulusBitLen, 32);
  }

 

  public void generate(int modulusBitLen, int publicKeyBitLen) throws Exception {
    int pBitLen = modulusBitLen / 16 * 8;
    int qBitLen = pBitLen + 8;
    BigInteger seedP, seedQ;
    boolean isAvail;
    do {
      seedP = BigInteger.probablePrime(pBitLen,
                                       new Random( System.currentTimeMillis()));
      seedQ = BigInteger.probablePrime(qBitLen,
                                       new Random( System.currentTimeMillis()));
      publicKey = BigInteger.probablePrime(publicKeyBitLen,
                                           new Random( System.currentTimeMillis()));
      setSeedP(seedP);
      setSeedQ(seedQ);
      choosePublicKey(publicKey);
      calculate();
      //now test vailable
      StringCryptor tool = CryptFactory.getStringCryptor(
          CryptFactory.ALGORITHMS_RSA, CryptFactory.ENCODING_UTF8, CryptFactory.PRESENT_STYLE_BASE32,
          new BigInteger[] {publicKey, privateKey, modulus});
      //here we swap the publicKey and privateKey

      String source1="第一条　中华人民共和国是工人阶级领导的、以工农联盟为基础的人民民主专政的社会主义国家。社会主义制度是中华人民共和国的根本制度。禁止任何组织或者个人破坏社会主义制度。" +
      		"第二条　中华人民共和国的一切权力属于人民。人民行使国家权力的机关是全国人民代表大会和地方各级人民代表大会。人民依照法律规定，通过各种途径和形式，管理国家事务，管理经济和文化事业，管理社会事务。" +
      		"第三条　中华人民共和国的国家机构实行民主集中制的原则。全国人民代表大会和地方各级人民代表大会都由民主选举产生，对人民负责，受人民监督。国家行政机关、审判机关、检察机关都由人民代表大会产生，对它负责，受它监督。中央和地方的国家机构职权的划分，遵循在中央的统一领导下，充分发挥地方的主动性、积极性的原则。" +
      		"第四条　中华人民共和国各民族一律平等。国家保障各少数民族的合法的权利和利益，维护和发展各民族的平等、团结、互助关系。禁止对任何民族的歧视和压迫，禁止破坏民族团结和制造民族分裂的行为。国家根据各少数民族的特点和需要，帮助各少数民族地区加速经济和文化的发展。各少数民族聚居的地方实行区域自治，设立自治机关，行使自治权。各民族自治地方都是中华人民共和国不可分离的部分。各民族都有使用和发展自己的语言文字的自由，都有保持或者改革自己的风俗习惯的自由。" +
      		"第五条　中华人民共和国实行依法治国，建设社会主义法治国家。国家维护社会主义法制的统一和尊严。一切法律、行政法规和地方性法规都不得同宪法相抵触。一切国家机关和武装力量、各政党和各社会团体、各企业事业组织都必须遵守宪法和法律。一切违反宪法和法律的行为，必须予以追究。任何组织或者个人都不得有超越宪法和法律的特权。" +
      		"第六条　 中华人民共和国的社会主义经济制度的基础是生产资料的社会主义公有制，即全民所有制和劳动群众集体所有制。社会主义公有制消灭人剥削人的制度，实行各尽所能、按劳分配的原则。国家在社会主义初级阶段，坚持公有制为主体、多种所有制经济共同发展的基本经济制度，坚持按劳分配为主体、多种分配方式并存的分配制度。" +
      		"第七条　国有经济，即社会主义全民所有制经济，是国民经济中的主导力量。国家保障国有经济的巩固和发展。" +
      		"第八条　农村集体经济组织实行家庭承包经营为基础、统分结合的双层经营体制。农村中的生产、供销、信用、消费等各种形式的合作经济，是社会主义劳动群众集体所有制经济。参加农村集体经济组织的劳动者，有权在法律规定的范围内经营自留地、自留山、家庭副业和饲养自留畜。城镇中的手工业、工业、建筑业、运输业、商业、服务业等行业的各种形式的合作经济，都是社会主义劳动群众集体所有制经济。国家保护城乡集体经济组织的合法的权利和利益，鼓励、指导和帮助集体经济的发展。" +
      		"第九条　矿藏、水流、森林、山岭、草原、荒地、滩涂等自然资源，都属于国家所有，即全民所有；由法律规定属于集体所有的森林和山岭、草原、荒地、滩涂除外。国家保障自然资源的合理利用，保护珍贵的动物和植物。禁止任何组织或者个人用任何手段侵占或者破坏自然资源。" +
      		"第十条　城市的土地属于国家所有。农村和城市郊区的土地，除由法律规定属于国家所有的以外，属于集体所有；宅基地和自留地、自留山，也属于集体所有。国家为了公共利益的需要，可以依照法律规定对土地实行征收或者征用并给予补偿。任何组织或者个人不得侵占、买卖或者以其他形式非法转让土地。土地的使用权可以依照法律的规定转让。一切使用土地的组织和个人必须合理地利用土地。" +
      		"第十一条　在法律规定范围内的个体经济、私营经济等非公有制经济，是社会主义市场经济的重要组成部分。国家保护个体经济、私营经济等非公有制经济的合法的权利和利益。国家鼓励、支持和引导非公有制经济的发展，并对非公有制经济依法实行监督和管理。" +
      		"第十二条　社会主义的公共财产神圣不可侵犯。国家保护社会主义的公共财产。禁止任何组织或者个人用任何手段侵占或者破坏国家的和集体的财产。" +
      		"第十三条　公民的合法的私有财产不受侵犯。国家依照法律规定保护公民的私有财产权和继承权。国家为了公共利益的需要，可以依照法律规定对公民的私有财产实行征收或者征用并给予补偿。" +
      		"第十四条　国家通过提高劳动者的积极性和技术水平，推广先进的科学技术，完善经济管理体制和企业经营管理制度，实行各种形式的社会主义责任制，改进劳动组织，以不断提高劳动生产率和经济效益，发展社会生产力。国家厉行节约，反对浪费。国家合理安排积累和消费，兼顾国家、集体和个人的利益，在发展生产的基础上，逐步改善人民的物质生活和文化生活。国家建立健全同经济发展水平相适应的社会保障制度。" +
      		"第十五条　国家实行社会主义市场经济。国家加强经济立法，完善宏观调控。国家依法禁止任何组织或者个人扰乱社会经济秩序。" +
      		"第十六条　国有企业在法律规定的范围内有权自主经营。国有企业依照法律规定，通过职工代表大会和其他形式，实行民主管理。" +
      		"第十七条　集体经济组织在遵守有关法律的前提下，有独立进行经济活动的自主权。集体经济组织实行民主管理，依照法律规定选举和罢免管理人员，决定经营管理的重大问题。" +
      		"第十八条　中华人民共和国允许外国的企业和其他经济组织或者个人依照中华人民共和国法律的规定在中国投资，同中国的企业或者其他经济组织进行各种形式的经济合作。在中国境内的外国企业和其他外国经济组织以及中外合资经营的企业，都必须遵守中华人民共和国的法律。它们的合法的权利和利益受中华人民共和国法律的保护。" +
      		"第十九条　国家发展社会主义的教育事业，提高全国人民的科学文化水平。国家举办各种学校，普及初等义务教育，发展中等教育、职业教育和高等教育，并且发展学前教育。国家发展各种教育设施，扫除文盲，对工人、农民、国家工作人员和其他劳动者进行政治、文化、科学、技术、业务的教育，鼓励自学成才。国家鼓励集体经济组织、国家企业事业组织和其他社会力量依照法律规定举办各种教育事业。国家推广全国通用的普通话。" +
      		"第二十条　国家发展自然科学和社会科学事业，普及科学和技术知识，奖励科学研究成果和技术发明创造。" +
      		"第二十一条　国家发展医疗卫生事业，发展现代医药和我国传统医药，鼓励和支持农村集体经济组织、国家企业事业组织和街道组织举办各种医疗卫生设施，开展群众性的卫生活动，保护人民健康。国家发展体育事业，开展群众性的体育活动，增强人民体质。" +
      		"第二十二条　国家发展为人民服务、为社会主义服务的文学艺术事业、新闻广播电视事业、出版发行事业、图书馆博物馆文化馆和其他文化事业，开展群众性的文化活动。国家保护名胜古迹、珍贵文物和其他重要历史文化遗产。" +
      		"第二十三条　国家培养为社会主义服务的各种专业人才，扩大知识分子的队伍，创造条件，充分发挥他们在社会主义现代化建设中的作用。" +
      		"第二十四条　国家通过普及理想教育、道德教育、文化教育、纪律和法制教育，通过在城乡不同范围的群众中制定和执行各种守则、公约，加强社会主义精神文明的建设。国家提倡爱祖国、爱人民、爱劳动、爱科学、爱社会主义的公德，在人民中进行爱国主义、集体主义和国际主义、共产主义的教育，进行辩证唯物主义和历史唯物主义的教育，反对资本主义的、封建主义的和其他的腐朽思想。" +
      		"第二十五条　国家推行计划生育，使人口的增长同经济和社会发展计划相适应。" +
      		"第二十六条　国家保护和改善生活环境和生态环境，防治污染和其他公害。国家组织和鼓励植树造林，保护林木。" +
      		"第二十七条　一切国家机关实行精简的原则，实行工作责任制，实行工作人员的培训和考核制度，不断提高工作质量和工作效率，反对官僚主义。一切国家机关和国家工作人员必须依靠人民的支持，经常保持同人民的密切联系，倾听人民的意见和建议，接受人民的监督，努力为人民服务。" +
      		"第二十八条　国家维护社会秩序，镇压叛国和其他危害国家安全的犯罪活动，制裁危害社会治安、破坏社会主义经济和其他犯罪的活动，惩办和改造犯罪分子。" +
      		"第二十九条　中华人民共和国的武装力量属于人民。它的任务是巩固国防，抵抗侵略，保卫祖国，保卫人民的和平劳动，参加国家建设事业，努力为人民服务。国家加强武装力量的革命化、现代化、正规化的建设，增强国防力量。" +
      		"第三十条　中华人民共和国的行政区域划分如下：（一）全国分为省、自治区、直辖市；（二）省、自治区分为自治州、县、自治县、市；（三）县、自治县分为乡、民族乡、镇。直辖市和较大的市分为区、县。自治州分为县、自治县、市。自治区、自治州、自治县都是民族自治地方。" +
      		"第三十一条　国家在必要时得设立特别行政区。在特别行政区内实行的制度按照具体情况由全国人民代表大会以法律规定。" +
      		"第三十二条　中华人民共和国保护在中国境内的外国人的合法权利和利益，在中国境内的外国人必须遵守中华人民共和国的法律。中华人民共和国对于因为政治原因要求避难的外国人，可以给予受庇护的权利。";
      long begin=System.currentTimeMillis();
      String code = tool.encrypt(source1);
      long mid=System.currentTimeMillis();
      isAvail = tool.decrypt(code).equals(source1);
      long end=System.currentTimeMillis();
      System.out.println("enctime="+(mid-begin)+"ms dectime="+(end-mid)+"ms pass="+isAvail);
    }
    while (!isAvail);
  }

  public void calculate() throws Exception {
    modulus = seedP.multiply(seedQ);
    // 用辗转相除法得到decryptKey
    BigInteger privateM = seedP.subtract(BigInteger.ONE).multiply(seedQ.
        subtract(BigInteger.ONE)); //(p-1)(q-1)
    if (publicKey.gcd(privateM).compareTo(BigInteger.ONE) > 0) {
      throw
          new Exception("Can not generate because wrong publicKey was choose.");
    }
    /*
     * 如果gcd(a,b)=d，则存在m,n，使得d = ma + nb，
     * 称呼这种关系为a、b组合整数d，m，n称为组合系数。
     * 当d=1时，有 ma + nb = 1 ，此时可以看出m是a模b的乘法逆元，n是b模a的乘法逆元。
     * (需保证m,a,b正数  n负数)
     *
     * 这里a为encryptKey   b为privateM
     * 初始a*1+b*0=publicKey
     * a*0+b*1=privateM
     * 利用利用辗转相除法把等式右边转为1,则左边得m
     * 如果m<0  则m=m+privateM
     */
    BigInteger m1 = BigInteger.ONE, m2 = BigInteger.ZERO, d1 = publicKey,
        d2 = privateM;
    BigInteger swap;
    while (!d1.equals(BigInteger.ONE) && !d1.equals(BigInteger.ZERO)) {
      swap = m1;
      m1 = m2.subtract(m1.multiply(d2.divide(d1)));
      m2 = swap;
      swap = d1;
      d1 = d2.mod(d1);
      d2 = swap;
    }
    privateKey = m1.mod(privateM);
  }

  public BigInteger getPrivateKey() {
    return privateKey;
  }

  public BigInteger getModulus() {
    return modulus;
  }

  public BigInteger getPublicKey() {
    return publicKey;
  }

}
