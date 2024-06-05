# pip install oracledb
import oracledb  # oracledb 라이브러리 임포트
import pandas as pd  # pandas 라이브러리 임포트
import matplotlib.pyplot as plt
import numpy as np
# import io
# import base64
from oracledb import cursor

# 폰트 설정
plt.rc("font", family='Malgun Gothic')
# 마이너스 폰트 설정
plt.rc("axes", unicode_minus=False)

def create_plot():
    # 오라클 DB에 연결
    con = oracledb.connect(user="c##travelmaker", password="travelmaker1",
                           dsn="192.168.0.32:1521/xe")  # DB에 연결 (호스트이름 대신 IP주소 가능)
    cursor = con.cursor()  # 연결된 DB 지시자(커서) 생성
    print('DB 연결 성공!')

    # select * from member 쿼리 실행 및 결과를 판다스 데이터프레임으로 변환
    query = "select * from member"
    cursor.execute(query)
    columns = [col[0] for col in cursor.description]  # 컬럼 이름 가져오기
    out_data = cursor.fetchall()  # 결과 가져오기

    # 파이차트할꺼 판다스 데이터프레임으로 변환
    df = pd.DataFrame(out_data, columns=columns)

    # select * from member 쿼리 실행 및 결과를 판다스 데이터프레임으로 변환
    query2 = "select * from trvl_lst"
    cursor.execute(query2)
    columns2 = [col[0] for col in cursor.description]  # 컬럼 이름 가져오기
    out_data2 = cursor.fetchall()  # 결과 가져오기

    # 바차트할꺼 판다스 데이터프레임으로 변환
    df2 = pd.DataFrame(out_data2, columns=columns2)

    # GENDER 컬럼의 값 변환
    df['GENDER'] = df['GENDER'].replace({'f': '여자', '여': '여자', 'm': '남자', '남': '남자'})

    gender_counts = df['GENDER'].value_counts()

    # 파이 차트 그리기 -----------------------------
    f = len(df[df['GENDER'] == '여자'])
    m = len(df[df['GENDER'] == '남자'])
    # gender_counts 데이터
    gender_counts = pd.Series([f, m], index=['여자', '남자'])

    # 수와 비율을 동시에 표시하는 함수 정의
    def func(pct, allvalues):
        absolute = int(np.round(pct / 100. * np.sum(allvalues)))
        return "{:.1f}%\n({:d}명)".format(pct, absolute)

    plt.figure(figsize=(8, 8))
    gender_counts.plot.pie(autopct=lambda pct: func(pct, gender_counts), startangle=90,
                           colors=['lightpink', 'lightblue'])
    plt.ylabel('')  # y축 레이블 제거
    # 스프링부트에 img경로 맞추기 ★
    plt.savefig('d:/kdit/projects/final_project/fifinal/final_project/src/main/resources/static/img/pie.png')
    plt.clf()  # 이전 차트를 초기화

    # 바 차트 전처리 시작
    # 'RAGION_NM' 열의 값들을 세기
    region_counts = df2['RAGION_NM'].value_counts()

    # 결과 출력
    # print(region_counts)

    # 막대 차트 생성
    plt.figure(figsize=(12, 8))
    region_counts.plot(kind='bar',
                       color=['red', 'orange', 'yellow', 'green', 'blue', 'navy', 'violet', 'purple', 'black'])
    plt.xlabel('지역')
    plt.ylabel('인원 수')
    plt.xticks(rotation=45)  # x축 라벨의 회전을 45도로 설정
    # 스프링부트에 img경로 맞추기 ★
    plt.savefig('d:/kdit/projects/final_project/fifinal/final_project/src/main/resources/static/img/bar.png')
    plt.clf()  # 이전 차트를 초기화

    # 커서 및 DB 연결 종료
    cursor.close()
    con.close()

    return "이미지 생성 성공"

if __name__ == "__main__":
    # create_plot()
    result = create_plot()
    if result:
        print(result)
    else:
        print("이미지 생성 실패")