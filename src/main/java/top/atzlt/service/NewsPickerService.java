package top.atzlt.service;

import top.atzlt.domain.NewsDomain;

import java.util.List;

public interface NewsPickerService {

    //获取所有新闻id
    public List<Integer> getAllNewsId();

    //获取最新的新闻id
    public int getLastNewsId();

    //获取前十条最新的新闻的id
    public List<Integer> getTopTenLastNewsId();

    //从当前客户端最新的新闻获取，最多获取十条id
    public List<Integer> getLastNewsIdsFromOldNewsId(int oldLastNewsId);

    //从当前客户端最久的新闻向下获取，最多获取十条id
    public List<Integer> getOldNewsIdsFromOldNewsId(int clientOldNewsId);

    //获取前十条最新的新闻
    public NewsDomain getTopTenLastNews();

    //从当前客户端最新的新闻获取，最多获取十条id
    public NewsDomain getLastNewsFromOldNewsId(int oldLastNewsId);

    //从当前客户端最久的新闻向下获取，最多获取十条id
    public NewsDomain getOldNewsFromOldNewsId(int clientOldNewsId);

    //获取当前最新的新闻
    public NewsDomain getLastNews();

    //获取前十条最新的新闻，只有id和title
    public NewsDomain getTopTenLastSimpleNews();

    public NewsDomain getNewsById(int newsId);

    public NewsDomain getNewsSimpleInfoFromNewsIdList(List<Integer> ids);

}
