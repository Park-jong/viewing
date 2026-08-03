package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyRequest;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import com.ssafy.interviewstudy.repository.study.StudyRequestFileRepository;
import com.ssafy.interviewstudy.repository.study.StudyRequestRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardService;
import com.ssafy.interviewstudy.service.study.studyMember.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberWithdrawalServiceImpl implements MemberWithdrawalService {

    private final MemberRepository memberRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final BoardRepository boardRepository;
    private final StudyRepository studyRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyRequestFileRepository studyRequestFileRepository;
    private final BoardService boardService;
    private final StudyMemberService studyMemberService;

    @Override
    public boolean withdraw(Integer memberId) {
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(MemberExceptionFactory::memberNotFound);

        List<Study> leadingStudies = studyRepository.findStudyByLeader(member);
        if (!leadingStudies.isEmpty()) return false;

        member.withdrawal();
        articleCommentRepository.deleteArticleCommentByAuthor(member);

        List<Board> articles = boardRepository.findAllByMember(member);
        for (Board article : articles) {
            boardService.removeArticle(article.getId());
        }

        List<Integer> studyIdList = studyRepository.findStudyIdByMember(member);
        for (Integer id : studyIdList) {
            studyMemberService.leaveStudy(id, memberId);
        }

        List<StudyRequest> requests = studyRequestRepository.findStudyRequestsByApplicant(member);
        for (StudyRequest request : requests) {
            studyRequestFileRepository.deleteByRequestId(request.getId());
            studyRequestRepository.deleteStudyRequestById(request.getId());
        }
        return true;
    }
}
