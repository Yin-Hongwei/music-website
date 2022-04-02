package com.example.yin;

import com.example.yin.domain.Consumer;
import com.example.yin.service.impl.ConsumerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YinMusicApplicationTests {

    @Autowired
    // private SongServiceImpl songService;
    // private SingerServiceImpl singerService;
    // private SongListServiceImpl songListService;
    private ConsumerServiceImpl consumerService;
    // private RankListServiceImpl rankListService;

    // @Test
    // public void contextLoads() {
    //
    // }

    // @Test
    // public void testRank(){
    // Rank rank_list = new Rank();
    // rank_list.setConsumerId(2L);
    // rank_list.setScore(3);
    // rank_list.setSongListId(2L);
    // rankListService.insert(rank_list);
    // System.out.println("歌单均分为"+rankListService.selectAverScore(2L));
    // }

    // 歌曲
    // @Test
    // public void songTest(){
    // Song song = new Song();
    // song.setName("Sanna Nielsen-Undo");
    // song.setPic("/img/songPic/1775711278864263.jpg");
    // song.setSingerId(42);
    // song.setCreateTime(new Date());
    // song.setUpdateTime(new Date());
    // song.setIntroduction("Undo");
    // song.setLyric("[00:00:00]暂无歌词");
    // song.setUrl("/song/Sanna Nielsen-Undo.mp3");
    // songService.addSong(song);
    // }

    // //歌手
    // @Test
    // public void singerTest(){
    // Singer singer = new Singer();
    // singer.setName("Álvaro Soler");
    // singer.setSex(new Byte("1"));
    // singer.setPic("/img/singerPic/soler.jpg");
    // singer.setBirth(new Date());
    // singer.setLocation("西班牙");
    // singer.setIntroduction("全名是Álvaro Tauchert
    // Soler，是一位新晋西班牙歌手，流行音乐作曲家。出生于1991年，西班牙巴塞罗纳。");
    // singerService.addSinger(singer);
    // }
    // @Test
    // public void singerTest2()
    // {
    // System.out.println(singerService.allSinger());
    // }

    // 歌单
    // @Test
    // public void songListTest(){
    //
    // SongList songList = new SongList();
    // songList.setTitle("国风传统器乐赏~~♪");
    // songList.setPic("/img/songListPic/19169985230816413.jpg");
    // songList.setIntroduction(" 都是自己很喜欢的吉他指弹");
    // songList.setStyle("乐器");
    // songListService.addSongList(songList);
    // }
    // @Test
    // public void songListTest2()
    // {
    // System.out.println(songListService.allSongList());
    // }

    // 用户
    @Test
    public void consumerTest() {

        Consumer consumer = new Consumer();
        consumer.setUsername("test");
        consumer.setPassword("123");
        consumer.setSex(new Byte("0"));
        consumer.setPhoneNum("15666412237");
        consumer.setEmail("1239679@qq.com");
        consumer.setBirth(new Date());
        consumer.setIntroduction("");
        consumer.setLocation("");
        consumer.setAvator("/img/avatorImages/user.jpg");
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());
        consumerService.addUser(consumer);
    }
    // @Test
    // public void consumerTest2()
    // {
    // System.out.println(consumerService.allUser());
    // }
}
